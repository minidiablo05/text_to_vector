🧠 Medical Multimodal AI Application


📌 Описание проекта
Данное приложение представляет собой backend-систему для обработки мультимодальных медицинских данных, реализованную на основе:
Spring Boot
Spring AI
Ollama


🎯 Цель проекта
Построение AI-пайплайна, который:
принимает MRI-данные (`.nii.gz`);
обрабатывает их через workflow;
преобразует в embeddings;
сохраняет в vector database (pgvector);
выполняет RAG (вопрос-ответ);
оценивает качество ответов.
---


🚀 Функциональность
загрузка MRI-файлов через REST API
pipeline обработки (workflow)
генерация embeddings
сохранение в vector store
поиск по контексту
RAG (вопрос-ответ)
evaluation (релевантность, точность, релевантность ответа)
---


🏗 Архитектура
```
MRI (.nii.gz)
   ↓
Validate
   ↓
Parse (MriParser)
   ↓
Normalize
   ↓
Embedding (Ollama)
   ↓
Vector Store (pgvector)
   ↓
RAG (ChatClient)
   ↓
Evaluation
```
---


⚙️ Технологии
Java 21
Spring Boot
Spring AI
Ollama
Chroma
Maven
---


🚀 Первый запуск проекта
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
./gradlew bootRun
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
---

⚠️ Ограничения
MriParser — базовый (placeholder)
нет полного анализа NIfTI
мультимодальность частичная
---

📌 Статус
MVP / Prototype
---

📌 Примечание
MRI-файлы не хранятся в репозитории.
