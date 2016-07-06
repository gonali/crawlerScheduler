CREATE TABLE crawlerTaskUserTable
(
  userId VARCHAR(64) NOT NULL,
  userAppkey TEXT,
  userDescription TEXT,
  PRIMARY KEY (userId)

);


CREATE TABLE crawlerTaskTable
(
  userId VARCHAR(64) NOT NULL,
  taskId VARCHAR(128) NOT NULL,
  taskType ENUM('UNSET', 'TOPIC', 'WHOLESITE') DEFAULT 'UNSET' NOT NULL,
  taskRemark TEXT,
  taskSeedUrl TEXT,
  taskCrawlerDepth INT,
  taskDynamicDepth INT,
  taskPass INT,
  taskWeight INT,
  taskStartTime TIMESTAMP,
  taskRecrawlerDays INT,
  taskTemplatePath TEXT,
  taskTagPath TEXT,
  taskSeedPath TEXT,
  taskConfigPath TEXT,
  taskClickRegexPath TEXT,
  taskProtocolFilterPath TEXT,
  taskSuffixFilterPath TEXT,
  taskRegexFilterPath TEXT,
  taskStatus ENUM('UNCRAWL', 'CRAWLING', 'CRAWLED', 'EXCEPTIOSTOP', 'NODEWAITING', 'INQUEUE') DEFAULT 'UNCRAWL' NOT NULL,
  taskDeleteFlag BOOL DEFAULT FALSE,
  taskSeedAmount INT,
  taskSeedImportAmount INT,
  taskCompleteTimes INT,
  taskInstanceThreads  INT DEFAULT 1 NOT NULL,
  taskNodeInstances  INT DEFAULT 1 NOT NULL,
  taskStopTime TIMESTAMP,
  taskCrawlerAmountInfo TEXT,
  taskCrawlerInstanceInfo TEXT,
  PRIMARY KEY (taskId)
);


CREATE TABLE crawlerTaskSlaveTable(
  slaveId VARCHAR(64) NOT NULL ,
  slaveUsername VARCHAR(64) DEFAULT 'slave' ,
  slavePassword VARCHAR(64) DEFAULT 'password',
  slaveIp       VARCHAR(18) DEFAULT '127.0.0.1',
  slaveSshPort int DEFAULT 22,
  slaveAppPath VARCHAR(1024) DEFAULT '~/',
  PRIMARY KEY (slaveId)
);


CREATE TABLE crawlerTaskConfigTable(
  configId INT NOT NULL AUTO_INCREMENT,
  redisHost VARCHAR(18),
  redisPort INT DEFAULT 6379,
  maxTaskQueueSize INT,
  maxTaskRun INT,
  maxHeartbeatTimeoutCount INT,
  slaveHeartbeatInterval INT,
  slaveAppScript VARCHAR(512) DEFAULT 'crawlerStart.sh',
  adminPassword VARCHAR(512),
  PRIMARY KEY (configId)
);