It is recommended to create new Eclipse workspace and copy .metadata from original DemoSite - this way everything will be ready to go.
Also remember to:
- add environmental variables for Maven (M2_HOME and probably path variable) and Java (java.home)
- set up MySql database and create connection according to build.properties (or edit this file)
	Current DB configuration:
	port: 3306
	name: broadleaf
	user: admin
	password: admin





(this is auto-generated readme, left it in case you can find something useful)
## The New Broadleaf Commerce Demo Site

This Maven project is meant to be used as a solid started project for any [Broadleaf Commerce](http://www.broadleafcommerce.org) application. It has many sensible defaults set up along with examples of how a fully functioning eCommerce site based on Broadleaf might work.

One typical way of using this project would be to follow the [Getting Started Guide](http://docs.broadleafcommerce.org/current/Getting-Started.html), which would walk you through using our pre-bundled Eclipse workspace.

However, if you would like to utilize your own workspace or IDE configuration, you may prefer to fork this project. This would give you the added benefit of being able to pull in upstream changes as we work to improve the DemoSite.

> Note: If you are going to fork this project, we recommend basing your work on the `master` branch, and not the develop branch. develop is our ongoing development branch and there are no guarantees of stability on it.
