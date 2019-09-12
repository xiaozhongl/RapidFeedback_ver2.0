# API

### /LoginServlet
* For login
* POST 
```
{
    username: // this key is named as email in RegisterServlet
    password:
}
```
* return
```
{
    login_ACK: // if > 0 then login succeed/ -2 then fail
    projectList:
    firstName:
    token:
}
```
### /RegisterServlet
* For register
* POST
```
{
    email:
    password:
    firstName:
    middleName:
    lastName:
}
```
* return
``` 
{
    register_ACK: //true: success/ false: fail
}
```
