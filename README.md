# Papermarks

Papermarks is an Android-first app concept for digitizing photographed book pages **without storing the photo**. A camera frame is used only transiently for OCR. The app stores book metadata, page numbers, OCR text, text-position data, highlights, notes, and Nostr sync payloads.

## Core product rules

- The app name is **Papermarks**.
- Books can be created by scanning an EAN-13 / ISBN-13 barcode.
- Page photos are never persisted locally or remotely.
- Each OCR page stores the book title, author(s), EAN/ISBN, page number, scan order, OCR text, and annotations.
- Highlights target text offsets, not image pixels.
- Data can be public plaintext Nostr events or private encrypted Nostr events.
- The user can configure up to five relays manually or import up to five relays from NIP-65 relay list metadata.
- Obtainium installation is supported through APK assets in GitHub Releases.

## Current package contents

This repository contains:

- Android/Kotlin project structure
- Jetpack Compose app shell
- Room data model
- OCR-first page model
- EAN/ISBN scanner service wrapper
- ML Kit text recognition wrapper
- annotation model based on text ranges
- Nostr event model and relay configuration interfaces
- NIP-46/local signer architecture interfaces
- GitHub Actions workflows for APK generation
- signing and Obtainium setup documentation

> Note: this ZIP is a repository package, not a GitHub release APK. To install via Obtainium, push this repository to GitHub, configure signing secrets, create a tag such as `v0.1.0`, and let the included workflow publish the signed APK as a GitHub Release asset.

## Quick start

```bash
unzip papermarks-repo.zip
cd papermarks
git init
git add .
git commit -m "Initial Papermarks repository"
git branch -M main
git remote add origin git@github.com:YOUR_USER/papermarks.git
git push -u origin main
```

Then configure release signing as described in [`docs/RELEASE_SIGNING.md`](docs/RELEASE_SIGNING.md), tag a release, and add the GitHub repository in Obtainium.

## Nostr kinds

Papermarks uses custom parameterized replaceable event kinds:

- `31990`: Book
- `31991`: OCR page text
- `31992`: Annotation
- `31993`: Relay profile / client settings mirror

## Example page payload

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
  "annotations": [
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
      "note": null,
      "createdAt": 1781950000,
      "updatedAt": 1781950000
    }
  ],
  "visibility": "private"
}
```

## Privacy

See [`docs/PRIVACY_MODEL.md`](docs/PRIVACY_MODEL.md).
