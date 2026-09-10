#!/usr/bin/env python3
"""Compare tracked files between two git repositories by content hash.

Usage:
    python3 compare_repos.py <repo1_path> <repo2_path> [--output report.md]

Reports, for the two repos:
  - files identical in both (same relative path and same content)
  - files at the same relative path with different content
  - files unique to each repo
  - same content duplicated under a different relative path across repos
"""
import argparse
import hashlib
import subprocess
import sys
from pathlib import Path


def list_tracked_files(repo: Path) -> list[str]:
    result = subprocess.run(
        ["git", "-C", str(repo), "ls-files"],
        capture_output=True, text=True, check=True,
    )
    return [line for line in result.stdout.splitlines() if line]


def hash_file(path: Path) -> str:
    digest = hashlib.sha256()
    with path.open("rb") as f:
        for chunk in iter(lambda: f.read(65536), b""):
            digest.update(chunk)
    return digest.hexdigest()


def build_index(repo: Path) -> dict[str, str]:
    return {rel: hash_file(repo / rel) for rel in list_tracked_files(repo)}


def compare(repo1: Path, repo2: Path) -> dict:
    index1, index2 = build_index(repo1), build_index(repo2)
    paths1, paths2 = set(index1), set(index2)

    identical, modified = [], []
    for rel in sorted(paths1 & paths2):
        (identical if index1[rel] == index2[rel] else modified).append(rel)

    only_in_1 = sorted(paths1 - paths2)
    only_in_2 = sorted(paths2 - paths1)

    hashes1, hashes2 = {}, {}
    for rel, h in index1.items():
        hashes1.setdefault(h, []).append(rel)
    for rel, h in index2.items():
        hashes2.setdefault(h, []).append(rel)
    cross_duplicates = sorted(
        (h, sorted(hashes1[h]), sorted(hashes2[h]))
        for h in hashes1.keys() & hashes2.keys()
    )

    return {
        "identical": identical,
        "modified": modified,
        "only_in_1": only_in_1,
        "only_in_2": only_in_2,
        "cross_duplicates": cross_duplicates,
    }


def bullet_list(items: list[str]) -> list[str]:
    return [f"- {item}" for item in items] if items else ["_none_"]


def render_report(repo1: Path, repo2: Path, result: dict) -> str:
    lines = [
        f"# File review: {repo1.name} vs {repo2.name}",
        "",
        f"- `{repo1}`: {len(list_tracked_files(repo1))} tracked files",
        f"- `{repo2}`: {len(list_tracked_files(repo2))} tracked files",
        "",
        f"## Identical files ({len(result['identical'])})",
        *bullet_list(result["identical"]),
        "",
        f"## Same path, different content ({len(result['modified'])})",
        *bullet_list(result["modified"]),
        "",
        f"## Only in {repo1.name} ({len(result['only_in_1'])})",
        *bullet_list(result["only_in_1"]),
        "",
        f"## Only in {repo2.name} ({len(result['only_in_2'])})",
        *bullet_list(result["only_in_2"]),
        "",
        f"## Same content, different path ({len(result['cross_duplicates'])})",
    ]
    if result["cross_duplicates"]:
        for _, paths1, paths2 in result["cross_duplicates"]:
            lines.append(f"- {repo1.name}: {', '.join(paths1)} <-> {repo2.name}: {', '.join(paths2)}")
    else:
        lines.append("_none_")
    lines.append("")
    return "\n".join(lines)


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("repo1", type=Path)
    parser.add_argument("repo2", type=Path)
    parser.add_argument("--output", type=Path, help="write report to this file instead of stdout")
    args = parser.parse_args()

    result = compare(args.repo1.resolve(), args.repo2.resolve())
    report = render_report(args.repo1.resolve(), args.repo2.resolve(), result)

    if args.output:
        args.output.write_text(report)
    else:
        print(report)
    return 0


if __name__ == "__main__":
    sys.exit(main())
