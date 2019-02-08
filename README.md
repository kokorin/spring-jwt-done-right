# Spring JWT integration done right

On github you can find many examples how to integrate JWT with Spring.

Many of them use OncePerRequestFilter with custom logic to authenticate user
 which put Authentication into SecurityContextHolder.

Such solutions IMHO are not beautiful and are error-prone.

This repository shows how to do the same but using standard Spring Security filter:
 UsernamePasswordAuthenticationFilter and AbstractPreAuthenticatedProcessingFilter

# How to use

## Login (get JWT)

```
curl -X POST http://localhost:8080/login -v -d username=user -d password=user
```

Check AUTHORIZATION header

## Invoke (use JWT)

```
curl http://localhost:8080/greet -H "Authorization: Bearer JWT_YOU_RECEIVED_ON_LOGIN" -v
```

## Check JWT expiration

Use the following JWT: `eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNTQ5NjM1NjMyLCJleHAiOjE1NDk2MzU2NDIsImF1dGhvcml0aWVzIjoiUk9MRV9VU0VSIn0.aVJVydkEAO-sJ29_7SveFTDipl2yEcmAIZt-DyB6UV0`:

```
curl http://localhost:8080/greet -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNTQ5NjM1NjMyLCJleHAiOjE1NDk2MzU2NDIsImF1dGhvcml0aWVzIjoiUk9MRV9VU0VSIn0.aVJVydkEAO-sJ29_7SveFTDipl2yEcmAIZt-DyB6UV0" -v
```
