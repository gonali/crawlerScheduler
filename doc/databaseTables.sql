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
  taskName VARCHAR(1024) DEFAULT NULL,
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
  taskTagPath TEXT DEFAULT NULL,
  taskSeedPath TEXT DEFAULT NULL,
  taskConfigPath TEXT DEFAULT NULL,
  taskClickRegexPath TEXT DEFAULT NULL,
  taskProtocolFilterPath TEXT DEFAULT NULL,
  taskSuffixFilterPath TEXT DEFAULT NULL,
  taskRegexFilterPath TEXT DEFAULT NULL,
  taskStatus ENUM('UNCRAWL', 'CRAWLING', 'CRAWLED', 'EXCEPTIOSTOP', 'NODEWAITING', 'INQUEUE') DEFAULT 'UNCRAWL' NOT NULL,
  taskDeleteFlag BOOL DEFAULT FALSE,
  taskSeedAmount INT,
  taskSeedImportAmount INT,
  taskCompleteTimes INT,
  taskInstanceThreads  INT DEFAULT 1 NOT NULL,
  taskNodeInstances  INT DEFAULT 1 NOT NULL,
  taskStopTime TIMESTAMP,
  taskCrawlerAmountInfo TEXT DEFAULT NULL,
  taskCrawlerInstanceInfo TEXT DEFAULT NULL,
  PRIMARY KEY (taskId)
);


CREATE TABLE crawlerTaskSlaveTable(
  slaveId VARCHAR(64) NOT NULL ,
  slaveUsername VARCHAR(64) DEFAULT 'slave' ,
  slavePassword VARCHAR(64) DEFAULT 'password',
  slaveIp       VARCHAR(18) DEFAULT '127.0.0.1',
  slaveSshPort int DEFAULT 22,
  slaveAppPath VARCHAR(1024) DEFAULT '~/',
  UNIQUE KEY (slaveIp, slaveUsername),
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
  slaveAppScript VARCHAR(512) DEFAULT '$HOME/crawlerStart.sh',
  adminPassword VARCHAR(512) DEFAULT '21232f297a57a5a743894a0e4a801fc3', /* default value is the md5 of 'admin' */
  PRIMARY KEY (configId)
);