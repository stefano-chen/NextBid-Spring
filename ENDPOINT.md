POST /api/auth/signup
POST /api/auth/signin
GET /api/auth/logout

GET /api/whoami

GET /api/users?q=query
GET /api/users/{id}
GET /api/users/{id}/auctions
GET /api/users/{id}/auctions/won
PUT /api/users/bio

POST /api/auctions
GET /api/auctions?q=query
GET /api/auctions/{id}  
PUT /api/auctions/{id}
DELETE /api/auctions/{id}

GET /api/auctions/{id}/bids 
POST /api/auctions/{id}/bids
GET /api/bids/{id}