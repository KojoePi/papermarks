# Papermarks JSON payloads

## Book

```json
{
  "schema": "papermarks.book.v1",
  "bookId": "isbn:9783442178582",
  "ean13": "9783442178582",
  "isbn13": "9783442178582",
  "title": "Example Book",
  "authors": ["Example Author"],
  "language": "de",
  "visibility": "PRIVATE",
  "createdAt": 1781950000,
  "updatedAt": 1781950000
}
```

## Page

```json
{
  "schema": "papermarks.page.v1",
  "book": {
    "bookId": "isbn:9783442178582",
    "ean13": "9783442178582",
    "isbn13": "9783442178582",
    "title": "Example Book",
    "authors": ["Example Author"]
  },
  "page": {
    "pageId": "sha256:...",
    "pageNumber": 42,
    "scanIndex": 7,
    "ocrText": "Recognized text...",
    "ocrBlocks": [],
    "contentFingerprint": "sha256:...",
    "createdAt": 1781950000,
    "updatedAt": 1781950000
  },
  "annotations": [],
  "visibility": "PRIVATE"
}
```

## Annotation

```json
{
  "annotationId": "anno_01",
  "type": "highlight",
  "target": {
    "kind": "textRange",
    "startOffset": 15,
    "endOffset": 32,
    "selectedText": "marked text",
    "prefix": "before ",
    "suffix": " after"
  },
  "style": {"color": "yellow"},
  "note": "A private note",
  "createdAt": 1781950000,
  "updatedAt": 1781950000
}
```
