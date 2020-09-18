spring:
 datasource:
  driver-class-name: ${database.driverName}
  url: ${database.url}
  username: ${database.username}
  password: ${database.password}
  
 jpa:
  hibernate:
   naming:
    physical-strategy: ${package}.RealNamingStrategyImpl
