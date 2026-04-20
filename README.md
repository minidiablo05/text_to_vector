# Text to Vector

Проект **Text to Vector** — это Spring Boot-приложение для работы с медицинскими текстовыми записями. Его основная идея заключается в том, чтобы:

- принимать историю болезни пациента в виде текста;
- преобразовывать текст в векторное представление;
- сохранять данные для последующего поиска;
- находить похожие записи по текстовому запросу.

Проект построен как веб-приложение и использует REST API для взаимодействия с клиентом.

## Стек технологий

- **Java 21**
- **Spring Boot**
- **Spring Web**
- **Maven**
- **WAR packaging**
- **Ollama** — для локальной работы с LLM
- **Chroma Vector Database** — для хранения и поиска векторных представлений

## Архитектура проекта

Основные компоненты приложения:

- `TextToVectorApplication` — точка входа в приложение;
- `ServletInitializer` — используется для запуска приложения в servlet-контейнере при упаковке в WAR;
- `MedicalRecordController` — принимает HTTP-запросы для сохранения и поиска записей;
- `MedicalRecordDTO` — объект передачи данных для медицинской записи;
- `MedicalRecordService` — интерфейс бизнес-логики;
- `MedicalRecordServiceImpl` — реализация логики сохранения и поиска;
- `DatasetIngestController` — контроллер для загрузки датасета;
- `DatasetIngestService` и `DatasetIngestServiceImpl` — сервис для загрузки JSONL-файлов и массового добавления записей.

## Возможности

На текущем этапе приложение поддерживает:

- сохранение медицинской записи в векторное пространство;
- поиск похожих медицинских записей по текстовому запросу;
- подготовку архитектуры для загрузки датасета из файла.

## Первый запуск проекта
1. Требования
Java 21
Docker
Ollama
---

2. Клонирование
```bash
git clone https://github.com/minidiablo05/text_to_vector.git
cd text_to_vector
```
---

3. Chroma
```bash
docker run -v ./chroma-data:/data -p 8000:8000 chromadb/chroma
```

4. Ollama
```bash
ollama serve
```
В новом терминале:
```bash
ollama pull llama3.2
ollama pull nomic-embed-text
```
---

5. Конфигурация
```yaml
spring:
  servlet:
    multipart:
      max-file-size: 50MB
      max-request-size: 50MB

  ai:
    model:
      chat: ollama
      embedding: ollama

    ollama:
      base-url: http://localhost:11434
      chat:
        options:
          model: llama3.2
      embedding:
        options:
          model: nomic-embed-text

    vectorstore:
      chroma:
        client:
          host: http://localhost
          port: 8000
        collection-name: my_collection
        initialize-schema: true
```
---

6. Файлы с данными.
```
data/augmented_notes_50.jsonl

```
---
7. Запуск
```bash
./mvnw spring-boot:run
```
---

8. Ingest file
```bash
curl -X POST "http://localhost:8080/api/ingest" 
   -F dataset=augmented-clinical-notes 
   -F sourceName=augmented_notes_50.jsonl 
   -F file=@"data\augmented_notes_50.jsonl"

```
---

9. QA
```bash
curl --get "http://localhost:8080/api/records/search" \
  --data-urlencode "query=neck pain headache"
```