#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"

section() {
  echo
  echo "======================================================================"
  echo "  $1"
  echo "======================================================================"
}

section "1. История коммитов: добавление интерактивного CLI"
git log --oneline --graph -5

section "2. Что именно добавили (diffstat последнего коммита)"
git show --stat HEAD

section "3. Живой запуск CLI: Clamp -> конвертация температуры -> скидка -> выход"
printf "1\n5\n1\n10\n3\n1\n0\n4\nSKU-002\n10\n0\n" | mvn -q compile exec:java

section "4. mvn verify: тесты + порог покрытия JaCoCo (95% строк / 85% ветвлений)"
mvn -q verify

section "5. Рабочее дерево чистое, изменений нет"
git status
