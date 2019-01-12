tar -zxvf elastic-job-lite-console-2.1.5.tar.gz 就获得了我们想要的运维平台部署代码。
切换进入bin/start.sh 就可以使用ip:8899端口查看  登录用户密码默认root/root
全局配置添加注册中心（zookeeper的地址:端口 命名空间一定要与任务对应得命名空间(regCenter.namespace=elasticJobDemo)一致，才能查看到该命名空间下的任务状态）