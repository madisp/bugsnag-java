package com.bugsnag;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;

import com.bugsnag.utils.JSONUtils;

public class Configuration {
    public class LockableValue<T> {
        private T value;
        private boolean locked = false;

        LockableValue() {}

        LockableValue(T initial) {
            this.value = initial;
        }

        public void set(T value) {
            if(!locked) this.value = value;
        }

        public void setLocked(T value) {
            this.value = value;
            locked = true;
        }

        public T get(T value) {
            set(value);
            return get();
        }

        public T get() {
            return value;
        }
    }

    protected static final String DEFAULT_ENDPOINT = "notify.bugsnag.com";

    private static final String NOTIFIER_NAME = "Java Bugsnag Notifier";
    private static final String NOTIFIER_VERSION = "1.2.8";
    private static final String NOTIFIER_URL = "https://bugsnag.com";

    // Notifier settings
    String notifierName = NOTIFIER_NAME;
    String notifierVersion = NOTIFIER_VERSION;
    String notifierUrl = NOTIFIER_URL;

    // Notification settings
    String apiKey;
    boolean autoNotify = true;
    boolean useSSL = false;
    String endpoint = DEFAULT_ENDPOINT;
    String[] notifyReleaseStages = null;
    String[] filters = new String[]{"password"};
    String[] projectPackages;
    String[] ignoreClasses;
    boolean sendThreads = false;

    // Before notify settings
    List<BeforeNotify> beforeNotify = new LinkedList<BeforeNotify>();

    // Error settings
    LockableValue<String> context = new LockableValue<String>();
    LockableValue<String> releaseStage = new LockableValue<String>("production");
    LockableValue<String> appVersion = new LockableValue<String>();
    LockableValue<Integer> appVersionCode = new LockableValue<Integer>();
    LockableValue<String> appId = new LockableValue<String>();
    LockableValue<String> osVersion = new LockableValue<String>();
    MetaData metaData = new MetaData();

    // User settings
    public JSONObject user = new JSONObject();

    // Logger
    public Logger logger = new Logger();

    public Configuration() {
    }

    public String getNotifyEndpoint() {
        return String.format("%s://%s", getProtocol(), endpoint);
    }

    public String getMetricsEndpoint() {
        return String.format("%s://%s/metrics", getProtocol(), endpoint);
    }

    public void addToTab(String tabName, String key, Object value) {
        this.metaData.addToTab(tabName, key, value);
    }

    public void clearTab(String tabName){
        this.metaData.clearTab(tabName);
    }

    public MetaData getMetaData() {
        return this.metaData.duplicate();
    }

    public boolean shouldNotify() {
        if(this.notifyReleaseStages == null)
            return true;

        List<String> stages = Arrays.asList(this.notifyReleaseStages);
        return stages.contains(this.releaseStage.get());
    }

    public boolean shouldIgnore(String className) {
        if(this.ignoreClasses == null)
            return false;

        List<String> classes = Arrays.asList(this.ignoreClasses);
        return classes.contains(className);
    }

    private String getProtocol() {
        return (this.useSSL ? "https" : "http");
    }

    public void setUser(String id, String email, String name) {
        JSONUtils.safePutOpt(this.user, "id", id);
        JSONUtils.safePutOpt(this.user, "email", email);
        JSONUtils.safePutOpt(this.user, "name", name);
    }

    public LockableValue<String> getContext() {
        return context;
    }

    public LockableValue<String> getOsVersion() {
        return osVersion;
    }

    public LockableValue<String> getAppVersion() {
        return appVersion;
    }

    public LockableValue<String> getReleaseStage() {
        return releaseStage;
    }

    public void setNotifyReleaseStages(String... notifyReleaseStages) {
        this.notifyReleaseStages = notifyReleaseStages;
    }

    public void setAutoNotify(boolean autoNotify) {
        this.autoNotify = autoNotify;
    }

    public void setUseSSL(boolean useSSL) {
        this.useSSL = useSSL;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public void setFilters(String... filters) {
        this.filters = filters;
    }

    public void setProjectPackages(String... projectPackages) {
        this.projectPackages = projectPackages;
    }

    public void setNotifierName(String notifierName) {
        this.notifierName = notifierName;
    }

    public void setNotifierVersion(String notifierVersion) {
        this.notifierVersion = notifierVersion;
    }

    public void setNotifierUrl(String notifierUrl) {
        this.notifierUrl = notifierUrl;
    }

    public void setIgnoreClasses(String... ignoreClasses) {
        this.ignoreClasses = ignoreClasses;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public void setSendThreads(boolean sendThreads) {
        this.sendThreads = sendThreads;
    }

    public void addBeforeNotify(BeforeNotify beforeNotify) {
        this.beforeNotify.add(beforeNotify);
    }
}
