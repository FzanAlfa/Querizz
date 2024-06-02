const {postPredictHandler, allHistories, registUsers, loginUser} = require('./handler');
 
const routes = [
  {
    path: '/register',
    method: 'POST',
    handler: registUsers,
  },
  {
    path: '/login',
    method: 'POST',
    handler: loginUser
  },
  {
    path: '/predict',
    method: 'POST',
    handler: postPredictHandler,
    options: {
      payload: {
        allow: 'multipart/form-data',
        maxBytes: 1000000,
        multipart: true,
      }
    }
  },
  {
    path: '/predict/histories',
    method: 'GET',
    handler: allHistories,
  },
  {
    method: 'GET',
    path: '/{param*}',
    handler: (request, h) => {
      return 'backend berhasil!';
    }
  },

]
 
module.exports = routes;
