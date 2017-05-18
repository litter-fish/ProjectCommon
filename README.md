# Spring + dubbo + mybatis 基本分层架构
项目分为五层，
CommonModule：公共代码
OpenAPI：提供对外服务接口
WebAPI：后台管理平台
ServerInterface：接口定义及以下bean定义
Server：接口实现及mybatis文件等

API层通过dubbo服务调用server层服务

# 文档
文档: https://github.com/litter-fish/ProjectCommon/wiki

# 项目配置
需要配置
ServerModule/Server/resources/conf/*.properties
