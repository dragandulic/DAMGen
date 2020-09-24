spring:
 jackson:
    serialization:
      fail-on-empty-beans: false
 jpa:
    hibernate:
      naming:
        physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
 datasource:
  driver-class-name: ${database.driverName}
  url: ${database.url}
  username: ${database.username}
  password: ${database.password}
  
