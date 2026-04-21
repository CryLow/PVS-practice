# Mutation Fuzzer

Инструмент для мутационного тестирования Java-кода на основе [Spoon](https://spoon.gforge.inria.fr/).

Берёт входной `.java` файл и генерирует набор мутированных версий, применяя каждую мутацию сразу ко **всем** вхождениям в файле.

## Поддерживаемые мутации

| Мутация | Описание |
|---------|----------|
| `ReplaceOperator` | Замена оператора сравнения `==` → `!=`, `<`, `>`, `<=`, `>=` |
| `ReplaceVarType` | Замена типа локальной переменной `int` → `long`, `float`, `double`, `short` |
| `AddParentheses` | Добавление скобок вокруг операндов `a == b` → `(a) == (b)` |
| `ComplexifyOperand` | Усложнение операндов `a == b` → `(a) + 1 == (b) + 1` |
| `AddCast` | Добавление приведения типа `a == b` → `(int)(a) == (int)(b)` |
| `WrapInContext` | Оборачивание выражения в `if`, `assert` или тернарный оператор |

## Структура проекта

```
mutation-fuzzer/
├── samples/                  # Входные Java-файлы для мутации
│   └── V6001TypicalCases.java
├── samples/out/              # Сгенерированные мутанты (создаётся автоматически)
├── src/main/java/            # Исходный код фаззера
└── build.gradle.kts
```

## Сборка

```powershell
.\gradlew.bat jar
```

JAR появится в `build/libs/mutation-fuzzer-1.0.0.jar`.

## Запуск

```powershell
java -jar build\libs\mutation-fuzzer-1.0.0.jar <input.java> <outputDir> <count>
```

**Пример:**
```powershell
java -jar build\libs\mutation-fuzzer-1.0.0.jar samples\V6001TypicalCases.java samples\out 100
```

- `input.java` — путь к исходному Java-файлу
- `outputDir` — папка для сохранения мутантов
- `count` — максимальное количество файлов на выходе

## Формат имён файлов

```
OriginalName_MutationId_variant.java
```

Например: `V6001TypicalCases_ReplaceOperator_eq_ne.java`
