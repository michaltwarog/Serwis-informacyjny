# Information-service

## Backend endpoints
### Login:
<ol>
    <li><b>GET /client/login (/editorial/login)</b></li>
        This endpoint allows the retrieval of a login page or a logout confirmation message depending on the presence of a query parameter named "logout".
    <br><br>
    <li><b>POST /client/login/v2 (/editorial/login/v2)</b></li>
        This endpoint allows a user to log in to the system. The login credentials must be provided in the request body in JSON format.
        If the login is successful, the client will be authorized.
    <br><br>
    <li><b>GET /client/login/google</b></li>
    This endpoint redirects the user to Google's authorization endpoint, allowing them to authenticate with their Google account. 
    The endpoint constructs an authorization URL with the required parameters, including the client ID and redirect URI.
    The user is redirected to this URL, where they can authenticate and grant access to the requested scopes.
    After successful authentication, the user is redirected back to the client application.
</ol>

### Registration:
<ol>
    <li><b>POST /client/registration (/editorial/registration)</b></li>
        This endpoint allows users to register an account in the system. 
        The endpoint expects a POST request with a JSON object representing the user registration data in the request body.
    <br><br>
    <li><b>POST /client/registration/fe (/editorial/registration/fc)</b></li>
        This endpoint handles user registration from an editorial microservice. It receives a POST request with a JSON payload containing user information. 
        It checks if the email or username already exists in the database, and if not, it registers the user. 
        It also checks if the request header "X-Caller" contains the value "REGISTRATION_FROM_EDITORIAL". 
        If it does not contain this value, the endpoint returns a bad request response. If the value is present, the endpoint returns a success response.
        It can only be accessed by the caller with X-Caller header set to "REGISTRATION_FROM_EDITORIAL" ("REGISTRATION_FROM_CLIENT"). So it <b>SHOULD NOT BE DIRECTLY ACCESSED!!!</b>
    <br><br>
    <li><b>GET /client/validate</b></li>
    This endpoint allows users to validate their account using a validation code. 
    The endpoint expects a GET request with the validation code as a query parameter.
    <br><br>
    <li><b>POST /editorial/validate</b></li>
    This endpoint allows enabling a user from the client application in the editorial microservice. 
    The endpoint expects a POST request with the user ID in the request body and the X-Caller header set to "ACCOUNT_ENABLE" to authorize the operation.
    <br><br>
</ol>

### Articles:
<ol>
    <li><b>GET /client/articles</b></li>
        This endpoint retrieves a list of articles. You can optionally filter the articles by category by providing a <b>category</b> query parameter.
    <br><br>
    <li><b>GET /client/articles/pages</b></li>
        This endpoint retrieves a paginated list of articles. You can optionally filter the articles by category by providing a category query
        parameter. You can also specify the <b>page</b> and <b>size</b> query parameters
        to control the pagination.
    <br><br>
    <li><b>POST /client/articles/fe</b></li>
    This endpoint is used to add an article from the editorial microservice to the client microservice.It can only be accessed by the caller with X-Caller header set to "ARTICLE_FROM_EDITORIAL". So it <b>SHOULD NOT BE DIRECTLY ACCESSED!!!</b>
    <br><br>
    <li><b>DELETE /client/articles/withdraw</b></li>
    This endpoint deletes an article from the client system and moves it to the editorial service.
    <br><br>
</ol>

### Users:
<ol>
    <li><b>GET /client/actions/user/info (/editorial/actions/user/info)</b></li>
    This endpoint retrieves the user information for the logged-in user. It checks if the user is authenticated and authorized to access their own information.
    <br><br>
    <li><b>GET /client/actions/get/users (/editorial/actions/get/users)</b></li>
    This endpoint retrieves a list of users from the database based on optional query parameters. Only users with ADMIN privileges can get a list of users.
    <br><br>
    <li><b>DELETE /client/actions/delete (/editorial/actions/delete)</b></li>
    This endpoint deletes a user from the database. Only users with ADMIN privileges or the owner of the account can delete a user.
    <br><br>
    <li><b>DELETE /client/actions/delete/fe (/editorial/actions/delete/fc)</b></li>
    This endpoint deletes a user from the database. It can only be accessed by the caller with X-Caller header set to "DELETE_FROM_EDITORIAL". So it <b>SHOULD NOT BE DIRECTLY ACCESSED!!!</b>
    <br><br>
    <li><b>PUT /client/actions/edit (/editorial/actions/edit)</b></li>
    This endpoint edits an existing user in the database. Only users with ADMIN privileges or the owner of the account can edit a user.
    <br><br>
    <li><b>DELETE /client/actions/edit/fe (/editorial/actions/edit/fc)</b></li>
    This endpoint edits a user from the database. It can only be accessed by the caller with X-Caller header set to "EDIT_FROM_EDITORIAL". So it <b>SHOULD NOT BE DIRECTLY ACCESSED!!!</b>
</ol>

### Editorial:
<ol>
    <li><b>GET /editorial/correct</b></li>
    This endpoint retrieves a list of article corrects.
    <br><br>
    <li><b>PUT /editorial/correct</b></li>
    This endpoint allows users to update an existing article correct.
    <br><br>
    <li><b>DELETE /editorial/correct/reject</b></li>
    This endpoint deletes an article correct and performs a move operation.
    <br><br>
    <li><b>DELETE /editorial/correct/accept</b></li>
    This endpoint deletes an article correct from the editorial system and moves it to the client service.
    <br><br>
    <li><b>POST /editorial/correct/fc</b></li>
    This endpoint is used to add an article correct from the client microservice to the editorial microservice.It can only be accessed by the caller with X-Caller header set to "ARTICLE_FROM_CLIENT". So it <b>SHOULD NOT BE DIRECTLY ACCESSED!!!</b>
    <br><br>
    <li><b>POST /editorial/proposal </b></li>
    This endpoint allows users to add a new article proposal to the system.
    <br><br>
    <li><b>PUT /editorial/proposal </b></li>
    This endpoint allows users to update an existing article proposal in the system.
    <br><br>
    <li><b>GET /editorial/proposal </b></li>
    This endpoint retrieves a list of article proposals.
    <br><br>
    <li><b>POST /editorial/draft</b></li>
    This endpoint allows users to initialize a new article draft.
    <br><br>
    <li><b>PUT /editorial/draft</b></li>
    This endpoint allows users to update an existing article draft.
    <br><br>
    <li><b>GET /editorial/draft</b></li>
    This endpoint retrieves a list of article drafts.
    <br><br>
    <li><b>DELETE /editorial/draft</b></li>
    This endpoint deletes an article draft and performs a move operation.
    <br><br>
</ol>