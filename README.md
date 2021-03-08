# Spring application/json-seq test

This is just a demo of using json sequences with spring mvc via spring boot. There seem to be multiple ways to do this with Spring this is just the first one I found that works.

## Usage

```
./gradlew bootRun
❯curl -v http://localhost:8080
* Rebuilt URL to: http://localhost:8080/
*   Trying ::1...
* TCP_NODELAY set
* Connected to localhost (::1) port 8080 (#0)
> GET / HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.54.0
> Accept: */*
>
< HTTP/1.1 200
< Content-Type: application/json-seq
< Transfer-Encoding: chunked
< Date: Mon, 08 Mar 2021 19:27:46 GMT
<
{"hello": "world"}
{"hello": "world"}
{"hello": "world"}
...
```

As you can see from above the content type is application/json-seq and if you run in real time it should be reasonably clear that the items are coming in one at a time. 

To confirm the sequence format pipe the curl through either `cat -v` or `od -c` to show the non printing chars. I prefer `od` because it's a bit more explicit

```
❯curl -v http://localhost:8080 | od -c
* Rebuilt URL to: http://localhost:8080/
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
  0     0    0     0    0     0      0      0 --:--:-- --:--:-- --:--:--     0*   Trying ::1...
* TCP_NODELAY set
* Connected to localhost (::1) port 8080 (#0)
> GET / HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.54.0
> Accept: */*
>
< HTTP/1.1 200
< Content-Type: application/json-seq
< Transfer-Encoding: chunked
< Date: Mon, 08 Mar 2021 19:28:59 GMT
<
{ [26 bytes data]
100 15900    0 15900    0     0   3520      0 --:--:--  0:00:04 --:--:--  35200000000  036   {   "   h   e   l   l   o   "   :       "   w   o   r   l
0000020    d   "   }  \n 036   {   "   h   e   l   l   o   "   :       "
0000040    w   o   r   l   d   "   }  \n 036   {   "   h   e   l   l   o
0000060    "   :       "   w   o   r   l   d   "   }  \n 036   {   "   h
0000100    e   l   l   o   "   :       "   w   o   r   l   d   "   }  \n
0000120  036   {   "   h   e   l   l   o   "   :       "   w   o   r   l
0000140    d   "   }  \n 036   {   "   h   e   l   l   o   "   :       "
0000160    w   o   r   l   d   "   }  \n 036   {   "   h   e   l   l   o
0000200    "   :       "   w   o   r   l   d   "   }  \n 036   {   "   h
0000220    e   l   l   o   "   :       "   w   o   r   l   d   "   }  \n
0000240  036   {   "   h   e   l   l   o   "   :       "   w   o   r   l
0000260    d   "   }  \n 036   {   "   h   e   l   l   o   "   :       "
0000300    w   o   r   l   d   "   }  \n 036   {   "   h   e   l   l   o
0000320    "   :       "   w   o   r   l   d   "   }  \n 036   {   "   h
0000340    e   l   l   o   "   :       "   w   o   r   l   d   "   }  \n
0000360  036   {   "   h   e   l   l   o   "   :       "   w   o   r   l
0000400    d   "   }  \n 036   {   "   h   e   l   l   o   "   :       "
0000420    w   o   r   l   d   "   }  \n 036   {   "   h   e   l   l   o
...
```

Here we can see the record separators and newlines demarcating the objects.

Note also that because we've used `produces = "application/json-seq""` in the handler, if we attempt to request a different content type then we'll get a 406. At some point we may enforce json seq in the framework so this is good practice.

```
curl -v http://localhost:8080 -H 'accept: application/json'
❯curl -v http://localhost:8080 -H 'accept: application/json'
* Rebuilt URL to: http://localhost:8080/
*   Trying ::1...
* TCP_NODELAY set
* Connected to localhost (::1) port 8080 (#0)
> GET / HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.54.0
> accept: application/json
>
< HTTP/1.1 406
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Mon, 08 Mar 2021 19:31:43 GMT
<
```