PluginPortalDemo
----------------

Purpose
===
Test if various OAuth2 based authentication providers can be integrated. In an
ideal world all OAuth2 based authentication providers would follow the OpenID
Connect protocol and thus we as service provider would only need the credentials
for each service and the backend URL and would be done. However, from the set
Google, Github and Amazon only Google seems to be compliant.

This demonstrates a strategy to keep the differences to a minimum.

Configuration
===
- Plan where you want to deploy the application at least for amazon it will need
  to have SSL capability
- The place where you deploy the application will be referred as the `baseUrl` it is:
  http(s)://<hostname>:<port>/
- Copy `config.template.yml` to `config.yml`
- Change `##BASE_URL##` to the value determined previously
- With the `baseUrl` create OAuth Credentials on Github, Amazon, Google and replace
  the `##CLIENT_ID_*##` and `##CLIENT_SECRET_*##` values with the value from
  the authentication providers. When you register the credentials, you will be
  asked for a redirect URI, this has to be set to `https://<hostname>:<port>/oauth/code`.
- Replace `##PATH_TO_JKS_STORE##` with the path to the JKS keystore, that contains
  the SSL key and certificate chain valid for `<hostname>`
- Replace `##PASSWORD##` with the password needed to open the JKS keystore

How to start the PluginPortalDemo application
===

1. Run `mvn clean install` to build your application
2. Start application with `java -jar target/PluginPortalDemo-1.0-SNAPSHOT.jar server config.yml`
3. To check that your application is running enter url `https://<hostname>:<port>/oauth`

Testing
===

1. Start the application
2. Open `https://<hostname>:<port>/oauth` - you will be presented with a list
   of available authentication providers
3. Open `https://<hostname>:<port>/oauth/start/<id>` (id is the id of one of the
   previously displayed entries. You will now be redirected to that authentication
   provider. If you are not yet logged in with that provider, you will be asked
   to do so. Then you are asked to allow access to your profile data (wording
   varies from provider to provider). After consenting you are redirected back
   to this sample application. The application verifies the login and fetches
   your profile data from the authentication provider and presents you with a
   JSON document holding the extracted data.

Logs will be written to `PluginPortalDemo.log`, access log will be written to
`PluginPortalDemo.access.log`.