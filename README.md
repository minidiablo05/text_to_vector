🧠 Medical Multimodal AI Application



Ссылка на данные используемые в проекте:

https://openneuro.org/datasets/ds007394/versions/1.0.0/file-display/sub-11:anat:sub-11_acq-MPRAGE_T1w.nii.gz
https://openneuro.org/datasets/ds007394/versions/1.0.0/file-display/sub-11:fmap:sub-11_acq-mb8_dir-ap_epi.nii.gz



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
PostgreSQL + pgvector
Gradle
---


🚀 Первый запуск проекта
1. Требования
Java 21
Docker
Ollama
---

2. Клонирование
```bash
git clone https://github.com/AlexeyLitovchenko/med_project.git
cd med_project
```
---

3. PostgreSQL + pgvector
```bash
docker run -d \
  --name pgvector-db \
  -p 5432:5432 \
  -e POSTGRES_DB=medvec \
  -e POSTGRES_USER=med \
  -e POSTGRES_PASSWORD=med \
  ankane/pgvector
```
```bash
docker exec -it pgvector-db psql -U med -d medvec
```
```sql
CREATE EXTENSION IF NOT EXISTS vector;
```
---

4. Ollama
```bash
ollama serve
```
В новом терминале:
```bash
ollama pull qwen2.5:7b
ollama pull nomic-embed-text
```
---

5. Конфигурация
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/medvec
    username: med
    password: med

  servlet:
    multipart:
      max-file-size: 512MB
      max-request-size: 512MB

  ai:
    ollama:
      base-url: http://localhost:11434
```
---

6. MRI файлы
```
data/sub-1_T1w.nii.gz
data/sub-1_FLAIR.nii.gz
```
---
7. Запуск
```bash
./gradlew bootRun
```
---

8. Ingest
```bash
curl -X POST "http://localhost:8080/api/ingest" \
  -F patientId=sub-1 \
  -F modality=MRI \
  -F sourceName=sub-1_T1w.nii.gz \
  -F file=@data/sub-1_T1w.nii.gz
```
---

9. QA
```bash
curl -X POST "http://localhost:8080/api/qa" \
  -H "Content-Type: application/json" \
  -d '{"question":"Какие MRI-данные загружены для пациента sub-1?"}'
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
