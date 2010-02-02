select @CLASS_ID := DOMAIN_CLASS_ID FROM FF$DOMAIN_CLASS_INFO WHERE DOMAIN_CLASS_NAME='pt.ist.expenditureTrackingSystem.domain.announcements.CCPAnnouncement';

alter table ANNOUNCEMENT add column OJB_CONCRETE_CLASS text;
update ANNOUNCEMENT set OJB_CONCRETE_CLASS='pt.ist.expenditureTrackingSystem.domain.announcements.CCPAnnouncement', OID = (@CLASS_ID << 32) + ID_INTERNAL;

update WORKFLOW_PROCESS WP, ANNOUNCEMENT A SET WP.OID_ANNOUNCEMENT = A.OID WHERE A.OID_ANNOUNCEMENT_PROCESS=WP.OID;