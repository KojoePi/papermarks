# Privacy Model

Papermarks is designed for private study notes and personally owned documents.

## Non-storage rule

The app must not persist photographed book pages:

- no local JPEG/PNG files
- no Room BLOBs containing images
- no cache files containing book pages
- no Blossom/NIP-96 uploads for page photos
- no Nostr event containing image URLs for page photos

The only persisted page data is text-derived:

- OCR text
- OCR block geometry
- page number
- scan index
- content fingerprint
- annotations and notes

## Private Nostr content

Private pages and annotations must encrypt `content`. The app should prefer NIP-44 encryption. Relay operators will still see event metadata such as pubkey, kind, tags, timestamp, and event size.

## Public content warning

Public pages expose recognized OCR text and annotations to anyone who can access the event through relays. The UI should make the public/private switch explicit before publishing.

## Copyright note

Papermarks should default to private storage. Publicly publishing OCR text from copyrighted books may be legally sensitive. The app should not encourage public distribution of copyrighted page text.
