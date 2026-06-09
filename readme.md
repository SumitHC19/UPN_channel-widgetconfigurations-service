### Channel Widget Configurations Service
channel-WidgetConfiguation service is the used to resolve all the client configurations like text, expression, link ,hro etc. Services can resolve client 
configurations using channel-widgetConfiguration by requesting configurations via dynamic config set. static config set Along with that, there is a specific support of adapter limited to header,footer and  tiles.

### Support information:
| | |
|---|---|
| **Service Owner/Group** | saurabh.agarwal.3@alight.com , Foundation Core Team|
| **Development Team**    | Foundation Core Team |
| **Technical Contact**   | saurabh.agarwal.3@alight.com |
| **JIRA Type** | [https://alightdevelopmentandit.atlassian.net/jira/software/c/projects/UFD/boards/231](https://alightdevelopmentandit.atlassian.net/jira/software/c/projects/UFD/boards/231)  Scrum Team 1 |
| **Support Contacts** |UPoint Portal Support Maestro Issue - CTS Portal Support|

### Home Page Usage: (Thrive/Standard/Not Used)
This service is extensively used in both Thrive and Standard Home page.
### Service Dependencies:

uServices :
  - Channel user profile (for new auth)
Docker Secrets :
  - REDIS_PWD_PF (Only in QC)
  - REDIS_PWD_SEC (Only in QC)
  - REDIS_PWD
  - app.rampart.keystore.password
  - udp.athz.token.alightonemobile.2021.04.23
  - udp.athz.token.upointmbl.2021.04.23
  - udp.authorize.token.upoint
  - logging.logstash.keypass
  - newrelic
  - Any secrets that has been used in HRO properties
 
REDIS envs:
  - REDIS_HOST_PF (Only in QC)
  - REDIS_PORT_PF (Only in QC) 
  - REDIS_DNS_HOST
  - REDIS_DNS_PORT
  - REDIS_MASTER
  - REDIS_NODES  

### Angular Widgets Calling this service
- Yes: This service is being called by all Angular widgets like header, footer, tiles etc.

### New Relic Dashboards
| | |
|---|---|
| **INT**|[int-channel-widgetconfigurations](https://one.newrelic.com/launcher/nr1-core.explorer?pane=eyJuZXJkbGV0SWQiOiJhcG0tbmVyZGxldHMub3ZlcnZpZXciLCJlbnRpdHlJZCI6Ik56VXlOams0ZkVGUVRYeEJVRkJNU1VOQlZFbFBUbnd4TWpBNE9URTBPRE0ifQ==)|
| **QA** |[qa-channel-widgetconfigurations](https://one.newrelic.com/launcher/nr1-core.explorer?pane=eyJuZXJkbGV0SWQiOiJhcG0tbmVyZGxldHMub3ZlcnZpZXciLCJlbnRpdHlJZCI6Ik56VXlOams0ZkVGUVRYeEJVRkJNU1VOQlZFbFBUbnd5TlRZNE5EQTBNelUifQ==)|
| **QC** |[qc-channel-widgetconfigurations](https://one.newrelic.com/launcher/nr1-core.explorer?pane=eyJuZXJkbGV0SWQiOiJhcG0tbmVyZGxldHMub3ZlcnZpZXciLCJlbnRpdHlJZCI6Ik56UTVOelk1ZkVGUVRYeEJVRkJNU1VOQlZFbFBUbnd4TURrd01qWTRPRE0ifQ==)|
| **PROD** |[prod-channel-widgetconfigurations](https://one.newrelic.com/launcher/nr1-core.explorer?pane=eyJuZXJkbGV0SWQiOiJhcG0tbmVyZGxldHMub3ZlcnZpZXciLCJlbnRpdHlJZCI6Ik56VXlOekF5ZkVGUVRYeEJVRkJNU1VOQlZFbFBUbnd4TWpBd05qTXpORFkifQ==)|

### Monitoring Alerts Specification:
- Standard Monitoring
            
### Service Failure Criticality: 
Core services components like link, text, expressions, parameters resolution will not work. 

### API spec
https://upoint-dv.alight.com/specmgmt/#/swaggerui-fullpage?service=channel-widgetconfigurations&version=1.107.0


#### Trigger Build
