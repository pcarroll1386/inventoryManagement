# inventoryManagement
Web App to track and request inventory.
Will be able to set up users and locations. Admin roles can add items and locations. Manager roles will be able to set up base items for their assigned locations.
User roles will be able to add inventory to thier location. Submit inventory requests. Set max and min's for items in location. 
The management program will be able to send inventory requests when inventory is at or below min.
Warehouse Roles will be able to pull all submitted requests to prepair items for locations and mark them as ready for pick up once the items have been staged.


App is not complete. 

Work done.
I have the database up and working using MySQL. 
I am using Java, Entities, Repositories and tests are complete, Spring Security is set up on back end to enable safe log in for users.
Working on Mock ups for the front end.

ToDo's
Mock Ups for front end,
Controller & Service Layer,
HTML, CSS, Javascript, Using a combination of Thymeleaf & JQuery.

Author 
Paul Frederick Carroll

## Inventory API Quickstart

Base path: /inventory

### 1) Create packaged inventory

POST /inventory/packaged

Sample request body:

```json
{
	"locationId": 1,
	"itemTypeId": 4,
	"nickname": "Beans Reserve",
	"quantity": 220
}
```

Expected response:

- 201 Created

### 2) Create bulk inventory

POST /inventory/bulk

Sample request body:

```json
{
	"locationId": 1,
	"itemTypeId": 6,
	"nickname": "Flour Bin A",
	"volumeMl": 240000.0,
	"volumeMeasurement": "KILOGRAMS"
}
```

Expected response:

- 201 Created

### 3) Get all packaged inventory

GET /inventory/packaged?userId=1

Optional filters:

- locationId (repeatable): /inventory/packaged?userId=1&locationId=1&locationId=2
- itemTypeId (repeatable): /inventory/packaged?userId=1&itemTypeId=4

Expected response:

- 200 OK
- JSON array of InventoryOverviewRow objects

### 4) Get all bulk inventory

GET /inventory/bulk?userId=1

Optional filters:

- locationId (repeatable)
- itemTypeId (repeatable)

Expected response:

- 200 OK
- JSON array of InventoryOverviewRow objects

### 5) Get all serialized inventory

GET /inventory/serialized?userId=1

Optional filters:

- locationId (repeatable)
- itemTypeId (repeatable)

Expected response:

- 200 OK
- JSON array of InventoryOverviewRow objects

### InventoryOverviewRow response shape

```json
[
	{
		"locationId": 1,
		"itemTypeId": 4,
		"itemName": "Canned Beans Case",
		"itemKind": "PACKAGED_CONSUMABLE",
		"quantity": 220,
		"volumeMl": null,
		"volumeMeasurement": null,
		"serialNumber": null,
		"status": null
	}
]
```

## Error Response Contract

All API errors now follow one consistent JSON shape.

### Error payload fields

- timestamp: ISO-8601 UTC timestamp when the error was generated
- status: HTTP status code
- error: HTTP reason phrase
- message: user-facing error description
- path: request path that failed
- details: optional field-level map for validation errors

### 400 Validation example

```json
{
	"timestamp": "2026-04-04T12:10:30.123Z",
	"status": 400,
	"error": "Bad Request",
	"message": "Validation failed.",
	"path": "/inventory/serialized",
	"details": {
		"serialNumber": "must not be blank",
		"locationId": "must not be null"
	}
}
```

### 404 Not Found example

```json
{
	"timestamp": "2026-04-04T12:11:02.441Z",
	"status": 404,
	"error": "Not Found",
	"message": "Please provide a valid user id.",
	"path": "/users/9999",
	"details": null
}
```
