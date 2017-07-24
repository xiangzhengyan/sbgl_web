--组织机构类型
insert into P_ORGANIZATION_TYPE (id, parent_code, sub_code)
values (1, 'organization', 'organization');
insert into P_ORGANIZATION_TYPE (id, parent_code, sub_code)
values (4, 'organization', 'employee');

--用户
insert into P_USER (id, login_name, real_name, password, telephone, email, sex, status)
values (829615482630, 'admin', '超级管理员', 'e10adc3949ba59abbe56e057f20f883e', null, null, null, 1);

--组织机构
insert into P_ORGANIZATION (id, name, parent_id, type,code)
values (174276394000, '总公司', null, 'organization','10');

--角色
insert into P_ROLE (id, code, name, enable)
values (1, 'ADMIN', '管理员', 1);


--用户分配角色
insert into P_USER_ROLE (id, user_id, role_id)
values (1, 829615482630, 1);


--用户加到组织机构
insert into p_organization_user(id, organization_id, user_id)
values(1,174276394000,829615482630);


--菜单
insert into P_FUNCTION (id, parent_id, label, url, app_id, menu_index, menu_level, enable, ico_name)
values (100, null, '系统平台', '#', null, 999, 1, 1, null);

insert into P_FUNCTION (id, parent_id, label, url, app_id, menu_index, menu_level, enable, ico_name)
values (1000, null, '设备管理', 'sbgl/sbgl.do', 100, 1, 1, 1, null);
insert into P_FUNCTION (id, parent_id, label, url, app_id, menu_index, menu_level, enable, ico_name)
values (2000, null, '组织权限', '#', 100, 2, 1, 1, null);
insert into P_FUNCTION (id, parent_id, label, url, app_id, menu_index, menu_level, enable, ico_name)
values (3000, null, '系统管理', '#', 100, 3, 1, 1, null);

insert into P_FUNCTION (id, parent_id, label, url, app_id, menu_index, menu_level, enable, ico_name)
values (2001, 2000, '用户管理', 'user/queryUserList.do', 100, 1, 3, 1, null);
insert into P_FUNCTION (id, parent_id, label, url, app_id, menu_index, menu_level, enable, ico_name)
values (2002, 2000, '组织机构', 'organization/queryOrganization.do', 100, 2, 3, 1, null);
insert into P_FUNCTION (id, parent_id, label, url, app_id, menu_index, menu_level, enable, ico_name)
values (2003, 2000, '角色管理', 'role/queryRoleList.do', 100, 3, 3, 1, null);

insert into P_FUNCTION (id, parent_id, label, url, app_id, menu_index, menu_level, enable, ico_name)
values (3001, 3000, '菜单管理', 'function/queryFunctionList.do', 100, 1, 2, 1, null);
insert into P_FUNCTION (id, parent_id, label, url, app_id, menu_index, menu_level, enable, ico_name)
values (3002, 3000, '数据字典', 'zd/goZdGroupItemPage.do', 100, 2, 2, 1, null);
insert into P_FUNCTION (id, parent_id, label, url, app_id, menu_index, menu_level, enable, ico_name)
values (3003, 3000, '系统参数', 'cfg/getAll.do', 100, 3, 2, 1, null);


--给角色授权菜单(这里开始）
insert into P_ROLE_FUNCTION (id, role_id, function_id)
values (1, 1, 100);
insert into P_ROLE_FUNCTION (id, role_id, function_id)
values (2, 1, 1000);
insert into P_ROLE_FUNCTION (id, role_id, function_id)
values (3, 1, 2000);
insert into P_ROLE_FUNCTION (id, role_id, function_id)
values (4, 1, 3000);
insert into P_ROLE_FUNCTION (id, role_id, function_id)
values (5, 1, 2001);
insert into P_ROLE_FUNCTION (id, role_id, function_id)
values (6, 1, 2002);
insert into P_ROLE_FUNCTION (id, role_id, function_id)
values (7, 1, 2003);
insert into P_ROLE_FUNCTION (id, role_id, function_id)
values (9, 1, 3001);
insert into P_ROLE_FUNCTION (id, role_id, function_id)
values (10, 1, 3002);
insert into P_ROLE_FUNCTION (id, role_id, function_id)
values (11, 1, 3003);


--数据字典
insert into P_ZD_GROUP (id, group_code, group_label, parent_code)
values (8, 'SF', '是否', null);
insert into P_ZD_GROUP (id, group_code, group_label, parent_code)
values (14, 'USER_STATUS', '用户状态', null);

insert into P_ZD_ITEM (id, label, code, allowdel, reserve_field, zd_group_code, parent_code, img)
values (1, '有效', '1', null, null, 'USER_STATUS', null, null);
insert into P_ZD_ITEM (id, label, code, allowdel, reserve_field, zd_group_code, parent_code, img)
values (2, '无效', '0', null, null, 'USER_STATUS', null, null);
insert into P_ZD_ITEM (id, label, code, allowdel, reserve_field, zd_group_code, parent_code, img)
values (3, '锁定', '2', null, null, 'USER_STATUS', null, null);
insert into P_ZD_ITEM (id, label, code, allowdel, reserve_field, zd_group_code, parent_code, img)
values (1105, '是', '1', null, null, 'SF', null, null);
insert into P_ZD_ITEM (id, label, code, allowdel, reserve_field, zd_group_code, parent_code, img)
values (1106, '否', '0', null, null, 'SF', null, null);



---------------------------------------------

--工具栏按钮

insert into P_FUNCTION_ACTION (id,label,code,parent_id)values (1, '-1', '-1', -1);

insert into P_FUNCTION_ACTION (id,label,code,Parent_Id)values(2, '新增', 'new', '2304');
insert into P_FUNCTION_ACTION (id,label,code,Parent_Id)values(3, '编辑', 'edit', '2304');
insert into P_FUNCTION_ACTION (id,label,code,Parent_Id)values(4, '查看', 'view', '2304');
insert into P_FUNCTION_ACTION (id,label,code,Parent_Id)values(5, '删除', 'delete', '2304');
insert into P_FUNCTION_ACTION (id,label,code,Parent_Id)values(6, '分配角色', 'authRole', '2304');

insert into P_FUNCTION_ACTION (id,label,code,Parent_Id)values(7, '新增', 'new', '2306');
insert into P_FUNCTION_ACTION (id,label,code,Parent_Id)values(8, '编辑', 'edit', '2306');
insert into P_FUNCTION_ACTION (id,label,code,Parent_Id)values(9, '删除', 'delete', '2306');
insert into P_FUNCTION_ACTION (id,label,code,Parent_Id)values(10, '授权菜单', 'authMenu', '2306');



insert into P_HOME_BY_ROLE (id, url, role_id)
values (1, '-1', -1);




insert into P_ORGANIZATION_ACTION (id, orga_fun_id, action_id)
values (1, -1, -1);

insert into P_ORGANIZATION_FUNCTION (id, organization_id, function_id)
values (1, -1, -1);

insert into P_ORGANIZATION_USER (id, organization_id, user_id)
values (1, -1, -1);

insert into P_ROLE_ACTION (id, role_fun_id, action_id)
values (1, -1, -1);
commit;


insert into P_ROLE_ACTION(id,ROLE_FUN_ID,Action_Id)values(2,6,2);
insert into P_ROLE_ACTION(id,ROLE_FUN_ID,Action_Id)values(3,6,3);
insert into P_ROLE_ACTION(id,ROLE_FUN_ID,Action_Id)values(4,6,4);
insert into P_ROLE_ACTION(id,ROLE_FUN_ID,Action_Id)values(5,6,5);
insert into P_ROLE_ACTION(id,ROLE_FUN_ID,Action_Id)values(6,6,6);

insert into P_ROLE_ACTION(id,ROLE_FUN_ID,Action_Id)values(7,9,7);
insert into P_ROLE_ACTION(id,ROLE_FUN_ID,Action_Id)values(8,9,8);
insert into P_ROLE_ACTION(id,ROLE_FUN_ID,Action_Id)values(9,9,9);
insert into P_ROLE_ACTION(id,ROLE_FUN_ID,Action_Id)values(10,9,10);
commit;

