# CP-Contests

This android app is used to see current running Competitive Programming Contests on some popular websites.

To successfully run the application, add the following values to **string.xml** file inside **res/values/** directory

```
<resources>
    ...
    <string name="clist_username">[USERNAME]</string>
    <string name="clist_key">[API_KEY]</string>
    
</resources>
```

To obtain [USERNAME] and [API_KEY] follow the given steps:
1. Go to **clist.by**
1. Create an account or login if you have one.
1. After logging-in, click your username on top-right corner and select API from the dropdown.
1. By clicking on get API key in authentication column, you will get the api key.
