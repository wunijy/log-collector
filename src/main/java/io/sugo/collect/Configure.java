package io.sugo.collect;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by fengxj on 3/29/17.
 */
public class Configure {
  private final Logger logger = LoggerFactory.getLogger(Configure.class);
  private static final String CLASSPATH_URL_PREFIX = "classpath:";
  private static final String COLLECT_PROPERTIES = "collect.properties";
  public static final String WRITER_CLASS = "writer.class";
  public static final String READER_CLASS = "reader.class";
  public static final String PARSER_CLASS = "parser.class";
  public static final String FILE_READER_BATCH_SIZE = "file.reader.batch.size";
  public static final String USER_DIR = "user.dir";

  private String collectorConf;
  private Properties properties = new Properties();

  public Configure() {
    collectorConf = System.getProperty(COLLECT_PROPERTIES, CLASSPATH_URL_PREFIX + COLLECT_PROPERTIES);
    loadConf();
  }

  private void loadConf() {
    try {
      if (collectorConf.startsWith(CLASSPATH_URL_PREFIX)) {
        collectorConf = StringUtils.substringAfter(collectorConf, CLASSPATH_URL_PREFIX);
        properties.load(Configure.class.getClassLoader().getResourceAsStream(collectorConf));
      } else {
        properties.load(new FileInputStream(collectorConf));
      }

      for (Object key : properties.keySet()) {
        logger.info(key + " : " + properties.getProperty(key.toString()));
      }

      if (!properties.contains(USER_DIR)){
        properties.put(USER_DIR, System.getProperty(USER_DIR));
      }
    } catch (IOException ix) {
      ix.printStackTrace();
    }
  }

  public String getProperty(String key) {
    return properties.getProperty(key);
  }
  public String getProperty(String key, String defaultValue) {
    if (!properties.containsKey(key))
      return defaultValue;
    return getProperty(key);
  }

  public Properties getProperties() {
    return this.properties;
  }

  public int getInt(String key, int defaultValue) {
    if (!properties.containsKey(key))
      return defaultValue;
    return getInt(key);
  }

  public int getInt(String key) {
    Object obj = this.properties.get(key);
    if (obj == null)
      return 0;
    try {
      return Integer.parseInt(this.properties.get(key).toString());
    } catch (Exception e) {
      logger.error("", e);
    }
    return 0;
  }
}
