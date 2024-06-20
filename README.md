

### Cara Menjalakan di Local  Host

1. Clone repository

    `git clone -b CC https://github.com/FzanAlfa/Querizz.git`

2. Install semua library pada json

    `npm i atau npm install` 

3. Update kunci/kunci.json

    `Update kunci.json dengan token auth dari firebase anda`

4. Buat file .env

    `isi dengan MODEL_URL anda`

5. Update file storedata.js

    `ubah bagian const customProjectId dan const customDatabaseId`

5. Jalankan

    `jalankan dengan perintah npm run start atau npm run start-dev`

### Notes : Perlu ada penyesuaiaan pada bagian inferenceService.js karna model yang kami gunakan hanya model contoh saja.
