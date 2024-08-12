package com.alinesno.infra.tool.backup.db;

import com.alinesno.infra.tool.backup.db.operation.HealthChecker;
import com.alinesno.infra.tool.backup.db.operation.UnhealthyResource;
import com.alinesno.infra.tool.backup.db.properties.ConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库备份
 */
@Slf4j
@SpringBootApplication
public class InspectionPlatformApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(InspectionPlatformApplication.class, args);
		log.debug("数据库备份程序启动...");
	}

	@Autowired
	private ConfigProperties properties ;

	public InspectionPlatformApplication(ConfigProperties properties) {
		this.properties = properties;
	}

	@Override
	public void run(String... args) throws Exception {

		HealthChecker checker = new HealthChecker();

		List<UnhealthyResource> resources = new ArrayList<>()  ;

		resources.add(new UnhealthyResource("权限配置服务", "web" ,"http://alinesno-infra-base-authority-ui.beta.base.infra.linesno.com", 80));
		resources.add(new UnhealthyResource("代码生成器", "web" ,"http://alinesno-infra-base-starter-ui.beta.base.infra.linesno.com", 80));
		resources.add(new UnhealthyResource("分布式配置服务", "web" ,"http://alinesno-infra-base-config-ui.beta.base.infra.linesno.com", 80));
		resources.add(new UnhealthyResource("事务消息服务", "web" ,"http://alinesno-infra-base-message-ui.beta.base.infra.linesno.com", 80));
		resources.add(new UnhealthyResource("通知管理服务", "web" ,"http://alinesno-infra-base-notice-ui.beta.base.infra.linesno.com", 80));
		resources.add(new UnhealthyResource("存储管理服务", "web" ,"http://alinesno-infra-base-storage-ui.beta.base.infra.linesno.com", 80));
		resources.add(new UnhealthyResource("单点登陆服务", "web" ,"http://alinesno-infra-base-identity-ui.beta.base.infra.linesno.com", 80));
		resources.add(new UnhealthyResource("网关配置服务", "web" ,"http://alinesno-infra-base-gateway-ui.beta.base.infra.linesno.com", 80));
		resources.add(new UnhealthyResource("安全验证码服务", "web" ,"http://alinesno-infra-base-validate-ui.beta.base.infra.linesno.com", 80));
		resources.add(new UnhealthyResource("IM消息服务", "web" ,"http://alinesno-infra-base-im-ui.beta.base.infra.linesno.com", 80));
		resources.add(new UnhealthyResource("邮箱管理服务", "web" ,"http://alinesno-infra-base-email-ui.beta.base.infra.linesno.com", 80));
		resources.add(new UnhealthyResource("向量库搜索服务", "web" ,"http://alinesno-infra-base-search-ui.beta.base.infra.linesno.com", 80));
		resources.add(new UnhealthyResource("分布式ID服务", "web" ,"http://alinesno-infra-base-id-ui.beta.base.infra.linesno.com", 80));
		resources.add(new UnhealthyResource("敏感词过滤服务", "web" ,"http://alinesno-infra-base-sensitive-ui.beta.base.infra.linesno.com", 80));
		resources.add(new UnhealthyResource("支付服务", "web" ,"http://alinesno-infra-base-pay-ui.beta.base.infra.linesno.com", 80));
		resources.add(new UnhealthyResource("会员服务", "web" ,"http://alinesno-infra-base-member-ui.beta.base.infra.linesno.com", 80));
		resources.add(new UnhealthyResource("内容管理系统服务", "web" ,"http://alinesno-infra-base-cms-ui.beta.base.infra.linesno.com", 80));
		resources.add(new UnhealthyResource("基础商品管理服务", "web" ,"http://alinesno-infra-base-shop-ui.beta.base.infra.linesno.com", 80));
		resources.add(new UnhealthyResource("流程中心服务", "web" ,"http://alinesno-infra-base-workflow-ui.beta.base.infra.linesno.com", 80));
		resources.add(new UnhealthyResource("文档查看服务", "web" ,"http://alinesno-infra-base-fileshow-ui.beta.base.infra.linesno.com", 80));
		resources.add(new UnhealthyResource("主数据服务", "web" ,"http://alinesno-infra-data-mdm-ui.beta.data.infra.linesno.com", 80));
		resources.add(new UnhealthyResource("数据上报服务", "web" ,"http://alinesno-infra-data-report-ui.beta.data.infra.linesno.com", 80));
		resources.add(new UnhealthyResource("数据集成服务", "web" ,"http://alinesno-infra-data-pipeline-ui.beta.data.infra.linesno.com", 80));
		resources.add(new UnhealthyResource("数据编排计算服务", "web" ,"http://alinesno-infra-data-dolphinscheduler-ui.beta.data.infra.linesno.com", 80));
		resources.add(new UnhealthyResource("实时计算服务", "web" ,"http://alinesno-infra-data-stream-ui.beta.data.infra.linesno.com", 80));
		resources.add(new UnhealthyResource("数据安全服务", "web" ,"http://alinesno-infra-data-security-ui.beta.data.infra.linesno.com", 80));
		resources.add(new UnhealthyResource("数据质量服务", "web" ,"http://alinesno-infra-data-quality-ui.beta.data.infra.linesno.com", 80));
		resources.add(new UnhealthyResource("数据总线服务", "web" ,"http://alinesno-infra-data-bus-ui.beta.data.infra.linesno.com", 80));
		resources.add(new UnhealthyResource("数据资产服务", "web" ,"http://alinesno-infra-data-assets-ui.beta.data.infra.linesno.com", 80));
		resources.add(new UnhealthyResource("数据算法服务", "web" ,"http://alinesno-infra-data-algorithm-ui.beta.data.infra.linesno.com", 80));
		resources.add(new UnhealthyResource("数据接口服务", "web" ,"http://alinesno-infra-data-fastapi-ui.beta.data.infra.linesno.com", 80));
		resources.add(new UnhealthyResource("OCR视觉识别服务", "web" ,"http://alinesno-infra-smart-ocr-ui.beta.smart.infra.linesno.com", 80));
		resources.add(new UnhealthyResource("自然语言识别服务", "web" ,"http://alinesno-infra-smart-nlp-ui.beta.smart.infra.linesno.com", 80));
		resources.add(new UnhealthyResource("大模型推理服务", "web" ,"http://alinesno-infra-smart-brain-ui.beta.smart.infra.linesno.com", 80));
		resources.add(new UnhealthyResource("流媒体识别服务", "web" ,"http://alinesno-infra-smart-media-ui.beta.smart.infra.linesno.com", 80));
		resources.add(new UnhealthyResource("智能助手服务", "web" ,"http://alinesno-infra-smart-assistant-ui.beta.smart.infra.linesno.com", 80));
		resources.add(new UnhealthyResource("目标检测识别服务", "web" ,"http://alinesno-infra-smart-detection-ui.beta.smart.infra.linesno.com", 80));
		resources.add(new UnhealthyResource("自动化任务服务", "web" ,"http://alinesno-infra-ops-scheduler-ui.beta.base.infra.linesno.com", 80));
		resources.add(new UnhealthyResource("分布式日志服务", "web" ,"http://alinesno-infra-ops-logback-ui.beta.base.infra.linesno.com", 80));
		resources.add(new UnhealthyResource("分布式链路跟踪服务", "web" ,"http://alinesno-infra-ops-telemetry-ui.beta.base.infra.linesno.com", 80));
		resources.add(new UnhealthyResource("持续集成服务", "web" ,"http://alinesno-infra-ops-pipeline-ui.beta.base.infra.linesno.com", 80));
		resources.add(new UnhealthyResource("容器管理服务", "web" ,"http://alinesno-infra-ops-container-ui.beta.base.infra.linesno.com", 80));
		resources.add(new UnhealthyResource("监控预警服务", "web" ,"http://alinesno-infra-ops-watcher-ui.beta.base.infra.linesno.com", 80));
		resources.add(new UnhealthyResource("商品秒杀服务", "web" ,"http://alinesno-infra-bus-limit-ui.beta.data.infra.linesno.com", 80));
		resources.add(new UnhealthyResource("实时推荐服务", "web" ,"http://alinesno-infra-bus-recommend-ui.beta.data.infra.linesno.com", 80));
		resources.add(new UnhealthyResource("实时画像服务", "web" ,"http://alinesno-infra-bus-profiling-ui.beta.data.infra.linesno.com", 80));
		resources.add(new UnhealthyResource("无代码开发服务", "web" ,"http://alinesno-infra-bus-nocode-ui.beta.data.infra.linesno.com", 80));
		resources.add(new UnhealthyResource("安全感触服务", "web" ,"http://alinesno-infra-plat-security-ui.beta.plat.infra.linesno.com", 80));
		resources.add(new UnhealthyResource("智能运营大脑服务", "web" ,"http://alinesno-infra-plat-security-ui.beta.plat.infra.linesno.com", 80));
		resources.add(new UnhealthyResource("基设平台管理服务", "web" ,"http://alinesno-infra-base-platform-ui.beta.base.infra.linesno.com", 80));
		resources.add(new UnhealthyResource("项目管理服务", "web" ,"http://alinesno-infra-plat-project-ui.beta.plat.infra.linesno.com", 80));
		resources.add(new UnhealthyResource("认证授权服务", "web" ,"http://alinesno-infra-base-identity-auth-application.beta.base.infra.linesno.com", 80));

		List<UnhealthyResource> results = checker.checkHealthOfWebsitesAndPorts(resources);
		for (UnhealthyResource resource : results) {
			log.debug("Unhealthy resource: {}", resource);
		}
	}

}
