# JavaRow
A Java library to connect to [Concept 2 rowing PM5 monitor with USB](https://www.concept2.com/indoor-rowers). 

# Use
To use the library, just get the Rower instance while being connected to a PM5 monitor through USB.
```java
Rower rower = Rower.getInstance();
```
It is then possible to communicate with the PM5 with predefined methods :
```java
rower.getMonitor(); // Get information that can be displayed on the monitor
rower.setWorkoutTime(Duration.ofMinutes(20)); // Ask the monitor to start a workout of 20 minutes
```
or by manually defining the commands to send to the monitor :
```java
Command command = new Command();
command.addCommand(ShortSpecificPMCommand.CSAFE_PM_GET_WORKOUTSTATE);
command.addCommand(ShortSpecificPMCommand.CSAFE_PM_GET_WORKOUTTYPE);
command.addCommand(ShortSpecificPMCommand.CSAFE_PM_GET_INTERVALTYPE);
command.addCommand(ShortSpecificPMCommand.CSAFE_PM_GET_WORKOUTINTERVALCOUNT);

UsbResponse response = rower.sendCommand(command);

Response.CSAFE_PM_GET_WORKOUTSTATE workoutState = (Response.CSAFE_PM_GET_WORKOUTSTATE) response.specificPMResponses.get(Response.SPECIFIC_PM_CODE.CSAFE_PM_GET_WORKOUTSTATE);
Response.CSAFE_PM_GET_WORKOUTTYPE workoutType = (Response.CSAFE_PM_GET_WORKOUTTYPE) response.specificPMResponses.get(Response.SPECIFIC_PM_CODE.CSAFE_PM_GET_WORKOUTTYPE);
Response.CSAFE_PM_GET_INTERVALTYPE intervalType = (Response.CSAFE_PM_GET_INTERVALTYPE) response.specificPMResponses.get(Response.SPECIFIC_PM_CODE.CSAFE_PM_GET_INTERVALTYPE);
Response.CSAFE_PM_GET_WORKOUTINTERVALCOUNT workoutIntervalCount = (Response.CSAFE_PM_GET_WORKOUTINTERVALCOUNT) response.specificPMResponses.get(Response.SPECIFIC_PM_CODE.CSAFE_PM_GET_WORKOUTINTERVALCOUNT);
```
All commands described in the Concept 2 USB protocol document are implemented in a object-oriented style.
