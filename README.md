# Quarkus Security Taglib

[![Maven Central](https://img.shields.io/maven-central/v/br.com.redcyclone/quarkus-security-taglib)](https://central.sonatype.com/artifact/br.com.redcyclone/quarkus-security-taglib)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)

Taglib for **Jakarta Faces (JSF)** that enables conditional rendering of components based on **Quarkus Security** roles, inspired by the Spring Security JSF taglibs.

> ⚠️ **Note**: Native Image compilation is not supported yet.

## 🚀 Installation

Add the dependency to your `pom.xml`:

```xml
<dependency>
  <groupId>br.com.redcyclone</groupId>
  <artifactId>quarkus-security-taglib</artifactId>
  <version>1.0.0</version>
</dependency>
```

## 🛠 Usage

Example:

```xml
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:h="jakarta.faces.html"
      xmlns:sec="quarkus.security">
  <h:body>
    <sec:authorize role="admin">
      <h:outputText value="Visible for users with the 'admin' role." />
    </sec:authorize>
    <br />
    <sec:authorize anyRole="admin, technician">
      <h:outputText value="Visible for users with either 'admin' or 'technician' roles." />
    </sec:authorize>
    <br />
    <sec:authorize allRoles="admin, technician">
      <h:outputText value="Visible for users with both the 'admin' and 'technician' roles." />
    </sec:authorize>
  </h:body>
</html>
```

## ⚙️ Configuration

No extra configuration is required. The taglib uses the roles contained in the `SecurityIdentity` instance.

## 📜 License
[Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0)
