# Connect using mycli (recommended for better UX)
    mycli -u pepe

# Or using standard MySQL client
    mysql -u pepe -p

```
➜  ~ mycli -u pepe
Connecting to socket /var/run/mysqld/mysqld.sock, owned by user mysql
Password: 
MariaDB 11.8.2
mycli 1.31.2
Home: http://mycli.net
Bug tracker: https://github.com/dbcli/mycli/issues
Thanks to the contributor - Carlos Afonso
MariaDB pepe@(none):(none)> 
```


---

### Create DB

    MariaDB pepe@(none):(none)> create database recruiting_db


### 👥 User Management & Permissions

-- Create application user (modern best practice)
```
CREATE USER 'your_username'@'%' IDENTIFIED BY 'your_password';
```

-- Grant necessary privileges (more secure than ALL PRIVILEGES)
```
GRANT ALL PRIVILEGES ON recruiting_db.* TO 'your_username'@'%';
```

-- Apply changes
```
FLUSH PRIVILEGES;
```


---

## 👤 Application Access

Option 1: Default Spring Security User
```
URL: http://localhost:8070/
User: user
Password: [check console for generated password]
```

    
Option 2: Custom Configured User
```
URL: http://localhost:8070/
User: admin
Password: your_password
```
