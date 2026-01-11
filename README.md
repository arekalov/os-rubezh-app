# ОС Рубеж - Wear OS Calculator App

Приложение для Wear OS с калькуляторами для решения задач по операционным системам.

## Технологический стек

- **Kotlin** - основной язык программирования
- **Jetpack Compose for Wear OS** - UI фреймворк
- **Clean Architecture** - архитектурный паттерн
- **Hilt** - dependency injection
- **Coroutines** - асинхронное программирование
- **StateFlow** - управление состоянием
- **ViewModel** - lifecycle-aware компоненты

## Архитектура

Проект следует принципам Clean Architecture с разделением на три слоя:

### Presentation Layer (`presentation/`)
- **Screens** - UI экраны с Compose
- **ViewModels** - управление состоянием и бизнес-логикой UI
- **Components** - переиспользуемые UI компоненты
- **Navigation** - навигация между экранами
- **Theme** - темная тема для Wear OS

### Domain Layer (`domain/`)
- **UseCases** - бизнес-логика приложения
- **Models** - модели данных

### Data Layer (`data/`)
- **Calculators** - реализация вычислительной логики

## Функциональность

Приложение содержит три калькулятора:

### 1. RAID Calculator
Рассчитывает время восстановления дискового массива RAID при замене одного диска.

**Входные параметры:**
- Уровень RAID (1, 5, 6)
- Количество дисков
- Размер диска (ГБ)
- Размер блока (байт)
- Размер stripe-unit (блоков)
- Скорость чтения блока (мкс)
- Скорость записи блока (мкс)
- Скорость вычисления stripe-unit (мкс)

**Выход:** Время восстановления в минутах

### 2. Pointers / Stripe Units Calculator
Вычисляет количество блоков, которое займет файл в UNIX-подобной файловой системе с учетом прямых и косвенных указателей в inode.

**Входные параметры:**
- Количество прямых указателей в inode
- Размер указателя (бит)
- Размер блока файловой системы (байт)
- Размер файла (байт)

**Выход:** Общее количество блоков (включая служебные)

### 3. NUMA Calculator
Рассчитывает уменьшение времени работы программы при оптимизации обращений к памяти в NUMA-архитектуре.

**Входные параметры:**
- Частота процессора (ГГц)
- Время обращения к L1-L2 кешу (нс)
- Время обращения к локальной NUMA памяти (нс)
- Время обращения к другим NUMA узлам (нс)
- Количество команд обращающихся к регистрам
- Количество команд обращающихся к L1-L2
- Количество команд обращающихся к local NUMA
- Количество команд обращающихся к other NUMA

**Выход:** Разница во времени выполнения (нс)

## Структура проекта

```
app/src/main/java/com/arekalov/osrubezh/
├── di/
│   └── AppModule.kt                    # Hilt модуль
├── presentation/
│   ├── theme/                          # Темная тема Wear OS
│   │   ├── Color.kt
│   │   ├── Theme.kt
│   │   └── Type.kt
│   ├── components/                     # Переиспользуемые компоненты
│   │   ├── NumericInputField.kt
│   │   ├── CalculatorButton.kt
│   │   └── ResultCard.kt
│   ├── navigation/                     # Навигация
│   │   ├── Navigation.kt
│   │   └── Screen.kt
│   ├── main/                          # Главный экран
│   │   └── MainScreen.kt
│   ├── raid/                          # RAID калькулятор
│   │   ├── RAIDScreen.kt
│   │   ├── RAIDViewModel.kt
│   │   └── RAIDUiState.kt
│   ├── pointers/                      # Pointers калькулятор
│   │   ├── PointersScreen.kt
│   │   ├── PointersViewModel.kt
│   │   └── PointersUiState.kt
│   └── numa/                          # NUMA калькулятор
│       ├── NUMAScreen.kt
│       ├── NUMAViewModel.kt
│       └── NUMAUiState.kt
├── domain/
│   ├── usecase/                       # Use cases
│   │   ├── CalculateRAIDUseCase.kt
│   │   ├── CalculatePointersUseCase.kt
│   │   └── CalculateNUMAUseCase.kt
│   └── model/                         # Модели данных
│       ├── RAIDResult.kt
│       ├── PointersResult.kt
│       └── NUMAResult.kt
└── data/
    └── calculator/                    # Калькуляторы
        ├── RAIDCalculator.kt
        ├── PointersCalculator.kt
        └── NUMACalculator.kt
```

## Особенности UI для Wear OS

- **ScalingLazyColumn** - оптимизированная прокрутка для круглых экранов
- **SwipeDismissableNavHost** - навигация со свайпом назад
- **Цифровые клавиатуры** - только цифровой ввод для всех полей
- **Темная тема** - оптимизирована для AMOLED дисплеев
- **Компактный дизайн** - адаптирован под маленький экран часов
- **Детализированные результаты** - возможность просмотра подробных расчетов

## Требования

- Android Studio Hedgehog или новее
- Минимальная версия Android: API 30 (Android 11 для Wear OS 3.0)
- Целевая версия: API 36

## Сборка и запуск

1. Клонируйте репозиторий
2. Откройте проект в Android Studio
3. Синхронизируйте Gradle
4. Запустите на эмуляторе Wear OS или реальных часах

## Preview

Каждый экран содержит Preview для быстрого просмотра в Android Studio:
- Круглые экраны (SMALL_ROUND)
- Квадратные экраны (SQUARE)

## Лицензия

Проект создан для образовательных целей.
