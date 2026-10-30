Day 6 - Validation

Completed Tasks
- Added Bean Validation annotations to employee request fields.
- Added validation for first name, last name, email, phone, joining date, and salary.
- Created a custom `@ValidEmployeeCode` annotation.
- Implemented `EmployeeCodeValidator` to validate the `EMP-1234` format.
- Added request body validation using `@Valid`.
- Added positive employee ID validation.
- Tested valid employee creation, update, and delete requests using Postman.
- Tested invalid employee codes, invalid phone number, missing email, missing first name, and future joining date.

Postman Testing
- Tested 3 valid employee codes.
- Tested 3 invalid employee codes.
- Tested additional invalid requests.
- Valid create request returned `201 Created`.
- Valid update request returned `200 OK`.
- Valid delete request returned `204 No Content`.
- Invalid requests returned `400 Bad Request`.

Self-Review
- Used request DTOs in controllers.
- Used constructor injection.
- Applied validation rules to employee request fields.
- Rejected invalid request data.
- Used meaningful names for classes and methods.