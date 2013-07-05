# Expenditures


Set of workflow	processes for managing financial workflows of institutions.
Current	workflow processes are: acquisitions and working capital management.


---
## Change Logs:

### On Develop

### v1.5.0

#### New Features

    [Working Capital] Added a restriction that prevents the creation of a new Working Capital, if the unit responsible is already unit responsible of another Working Capital from a previous year, that is still open.
    [Working Capital] Added a restriction that prevents the creation of a new Working Capital, if the movement responsible is already movement responsible of another Working Capital from a previous year, that is still open.
    [Working Capital] Added a restriction that prevents a requestor from creating a new Working Capital, if he/she is already requestor of another Working Capital from a previous year, that is still open.
    [Working Capital] Added a new restriction to prevent creating a new Working Capital for a unit that already has another open Working Capital for any year.
    [Working Capital] In every working capital process page or sub-page, added a link to the paying unit.
    [Missions] ProcessPersonalInformationActivity now supports processes in multiple queues.
    [Missions] Added a new automatic script to migrate authorizations of vehicle items from old mission processes.
    Improved unit visibility.

#### Enhancements

    Updated the dependency to the workflow 1.2.0
    [Missions] Renamed the VEHICLE_AUTHORIZATION MissionState
    [Missions] Removed the circular dependency in the MissionState VEHICLE_APPROVAL.
    The Unit comparator now distinguishes different instances.  

#### Bug Fixes

    [Missions] Corrected a possible problem with the migration of the MissionSystem that did not correctly reset the migrationInProgress variable if the code would throw an exception, or if the transaction would abort.
    [Missions] Corrected a problem with the automatic migration scripts for the personal information slot, and the verified slot, which did not correctly set the virtual host for thread.  

### v1.4.0

#### New Features

	[Missions] Added a new VERIFICATION MissionState to every MissionProcess. Added an activity to verify a process, and another activity to revert the verification.
	[Missions] Added a VerificiationQueue with the people that can verify processes. These people can view a list of verification-pending processes in the Missions front page. They receive email notifications with the verification-pending processes.
	[Missions] Added migration code to set old processes automatically as verified in certain cases.
	Added support for external provider when viewing a unit.
	
#### Enhancements
	
	[Missions] Changed the logic of the MissionYear's MissionProcessSearch to not use workflowQueues, but the MissionStates instead
	[Missions] Removed from all Mission activities preconditions, the references to workflowQueues under the presumption that the only possible queue is the personalInformationProcessing
	[Missions] Added automatic migration for the new Personal Information Processed slot
	[Missions] Added a new slot Mission.isPersonalInformationProcessed to help determine the state of processing for each process
	Incorporated external-accounting-integration module.
	Removed IST specific code for synchronizing  salary information.
	
#### Bug Fixes

	[Missions] Corrected a problem with the calculation of the MissionState for canceled MissionProcesses with no items
 	[Expenditures] Prevented the AllocateProjectFundsPermanently activity from running in canceled processes
	Other bug fixes and some code cleanup.

### v1.3.0

    Missions: Completed mission state refactor.
    Updated workflow module to v1.1.1.


### v1.2.0

    Updated version of fenix-framework to 2.1.0
    Missions: Refactor mission state view.
    Other bug fixes and enhancements.


### v1.1.0

    Expenditure tracking: Created second process verification phase.
    Expenditure tracking: Allow verification of all pending processes with a single user action.


### v1.0.0

	Added the following modules at version 1.0.0:
		Expenditure tracking
		Working capital
		Finance
		JodaFinance

