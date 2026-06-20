# Papermarks Architecture

## Goal

Papermarks is an OCR-first Android app for personal book annotation with Nostr sync. It does not store book-page photos.

## Capture pipeline

```text
Camera frame
→ optional perspective correction in memory
→ ML Kit OCR
→ OCR text + OCR block geometry
→ content fingerprint
→ discard frame
```

No image file, no bitmap blob, no Blossom upload, and no Nostr image URL are part of the persistence model.

## Domain model

- Book: EAN/ISBN, title, authors, language, visibility
- Page: page number, scan index, OCR text, OCR blocks, content fingerprint
- Annotation: text range, selected text, prefix/suffix repair context, color, note
- Relay settings: up to five manual or NIP-65 imported relays
- Signer: NIP-46, local nsec, or read-only npub

## Nostr event kinds

Papermarks uses parameterized replaceable events in the `30000-39999` range:

- `31990`: Book
- `31991`: Page OCR text
- `31992`: Annotation
- `31993`: Client settings mirror

Each event uses a `d` tag as the stable object ID.

## Public and private storage

Public events store plaintext JSON.

Private events store encrypted JSON. For private pages, the content should be encrypted with NIP-44. Tags should avoid leaking OCR text. Metadata leakage is still possible through timestamps, event kind, relay choice, and minimal tags.

## Annotation strategy

Highlights use text offsets:

```json
{
  "startOffset": 15,
  "endOffset": 32,
  "selectedText": "das einzige Wesen",
  "prefix": "Der Mensch ist ",
  "suffix": ", das über sich"
}
```

This makes annotations independent from screen size, OCR block geometry, and missing source photos.

## Login strategy

Recommended order:

1. NIP-46 / Nostr Connect remote signer
2. Local `nsec` import stored through Android Keystore
3. New key generation with explicit backup confirmation
4. `npub` read-only mode

## Relay strategy

Manual configuration permits up to five relays. Import mode reads NIP-65 relay list metadata (`kind:10002`) and picks up to five read/write relays.
