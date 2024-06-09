const { Firestore } = require('@google-cloud/firestore');

// Path to the service account key file
const serviceAccountPath = './kunci/kunci.json';
// Initialize Firestore with custom configuration
const db = new Firestore({
  projectId: 'nosqldatabase', // Your custom Firestore project ID
  keyFilename: serviceAccountPath // Path to your service account key file
});

async function registerUsers(email, data) {
  const usersCollection = db.collection("users");
  
  try {
    // Cek apakah email sudah terdaftar
    const userDoc = await usersCollection.doc(email).get();
    if (userDoc.exists) {
      throw new Error('Email sudah terdaftar, gunakan email lain');
    }

    // Jika email belum terdaftar, simpan data
    await usersCollection.doc(email).set(data);
  } catch (error) {
    // Jika terjadi error, kembalikan response error
    console.error('Error saat menyimpan data:', error);
    throw new Error(error.message);
  }
}

async function getUserByEmail(email, password) {
  const usersCollection = db.collection("users");
  
  try {
    // Cari pengguna berdasarkan email
    const userDoc = await usersCollection.doc(email).get();
    if (!userDoc.exists) {
      throw new Error('Email tidak terdaftar');
    }
    // Periksa kecocokan password
    const userData = userDoc.data();

    // Periksa kecocokan password
    if (userData.password !== password) {
      throw new Error('Password salah');
    }

    return userData;
  } catch (error) {
    console.error('Error saat mencari pengguna:', error);
    throw new Error(error.message);
  }
}
 
async function storeData(id, data, name) {
  const predictCollection = db.collection(name); // Mengakses koleksi dengan nama dinamis.
  return predictCollection.doc(id).set(data); // Menyimpan data ke dokumen dengan ID tertentu.
}

async function getAllData(nameUse) {
  const predictCollection = db.collection(nameUse);

  try {
    const snapshot = await predictCollection.get();
    const data = [];
    snapshot.forEach(doc => {
      data.push(doc.data());
    });
    return data;
  } catch (error) {
    console.error('Error saat mengambil data:', error);
    throw error;
  }
}

module.exports = {storeData, getAllData, registerUsers, getUserByEmail}
