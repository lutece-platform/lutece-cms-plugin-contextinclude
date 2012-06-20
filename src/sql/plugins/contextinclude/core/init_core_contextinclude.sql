--
-- Init  table core_admin_right
--
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url, documentation_url) VALUES ('CONTEXT_INCLUDE_MANAGEMENT','contextinclude.adminFeature.contextinclude_management.name',2,'jsp/admin/plugins/contextinclude/ManageContexts.jsp','contextinclude.adminFeature.contextinclude_management.description',0,'contextinclude','CONTENT','images/admin/skin/plugins/contextinclude/contextinclude.png', 'jsp/admin/documentation/AdminDocumentation.jsp?doc=admin-contextinclude');

--
-- Init  table core_user_right
--
INSERT INTO core_user_right (id_right,id_user) VALUES ('CONTEXT_INCLUDE_MANAGEMENT',1);
INSERT INTO core_user_right (id_right,id_user) VALUES('CONTEXT_INCLUDE_MANAGEMENT',2);
