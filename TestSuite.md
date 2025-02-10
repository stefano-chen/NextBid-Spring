# Test Suite
___
### AuthController Test

* HTTP POST /api/auth/signup with empty data --> HTTP code 400
* HTTP POST /api/auth/signup with missing data --> HTTP code 400
* HTTP POST /api/auth/signup with valid data --> HTTP code 200

* HTTP POST /api/auth/signin with empty data --> HTTP code 400
* HTTP POST /api/auth/signin with missing data --> HTTP code 400
* HTTP POST /api/auth/signin with valid data --> HTTP code 200


### UtilsController Test

* HTTP GET /api/whoami with authenticated user --> HTTP code 200
* HTTP GET /api/whoami with unauthenticated user --> HTTP code 400


### UsersController Test

* HTTP GET /api/users when there are no users --> []
* HTTP GET /api/users when there are users --> List of all users
* HTTP GET /api/users?q=mario when there are no users with matching query --> []
* HTTP GET /api/users?q=mario when there are users with matching query --> List of users that match the query

* HTTP GET /api/users/:id with valid id --> full detail
* HTTP GET /api/users/:id with invalid id --> "Invalid ID. Please check the ID and try again"

* HTTP PUT /api/users/bio with valid bio --> HTTP code 200
* HTTP PUT /api/users/bio with invalid bio --> HTTP code 400

* HTTP GET /api/users/:id/auctions with valid id --> List of all auction owned by the user identified by the provided id
* HTTP GET /api/users/:id/auctions with invalid id --> []

* HTTP GET /api/users/:id/auctions/won with valid id --> List of all auction won by the user identified by the provided id
* HTTP GET /api/users/:id/auctions/won with invalid id --> []


### AuctionsController Test

* HTTP POST /api/auctions with empty data --> HTTP code 400
* HTTP POST /api/auctions with missing data --> HTTP code 400
* HTTP POST /api/auctions with valid data --> HTTP code 200






