# Test Suite
___
### Endpoint existence
* HTTP POST /api/auth/signup --> HTTP code != 404
* HTTP POST /api/auth/signin --> HTTP code != 404
* HTTP GET /api/auth/logout --> HTTP code != 404
* HTTP GET /api/users/?q=query --> HTTP code != 404
* HTTP GET /api/users/:id --> HTTP code != 404
* HTTP POST /api/users/bio --> HTTP code != 404
* HTTP GET /api/auctions/?q=query --> HTTP code != 404
* HTTP POST /api/auctions --> HTTP code != 404
* HTTP GET /api/auctions/:id --> HTTP code != 404
* HTTP PUT /api/auctions/:id --> HTTP code != 404
* HTTP DELETE /api/auctions/:id --> HTTP code != 404
* HTTP GET /api/auctions/:id/bids --> HTTP code != 404
* HTTP POST /api/auctions/:id/bids --> HTTP code != 404
* HTTP GET /api/bids/:id --> HTTP code != 404
* HTTP GET /api/whoami --> HTTP code != 404

### Integration Test
* HTTP POST /api/auth/signup with valid data --> Session Cookie
* HTTP POST /api/auth/signin with valid data --> Session Cookie



### AuthController Test

* HTTP POST /api/auth/signup with empty data --> HTTP code 400
* HTTP POST /api/auth/signup with missing data --> HTTP code 400
* HTTP POST /api/auth/signup with valid data --> HTTP code 200

* HTTP POST /api/auth/signin with empty data --> HTTP code 400
* HTTP POST /api/auth/signin with missing data --> HTTP code 400
* HTTP POST /api/auth/signin with valid data --> HTTP code 200


