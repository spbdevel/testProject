package com.trial.properties.app.util;

import com.amazonaws.regions.Regions;

import java.net.MalformedURLException;
import java.net.URL;
import static com.trial.properties.app.util.Predefined.AllowedType.*;

public enum Predefined {
    JDBC_DRIVER("JDBC_DRIVER", STRING),
    JDBC_URL("JDBC_URL", STRING),
    JDBC_USERNAME("JDBC_USERNAME", STRING),
    JDBC_PASSWORD("JDBC_PASSWORD", STRING),
    HIBERNATE_GENERATE_STATISTICS("hibernate.generate_statistics", BOOLEAN),
    HIBERNATE_SHOW_SQL("hibernate.show_sql", BOOLEAN),

    AWS_ACCESS_KEY("aws_access_key", STRING),
    AWS_SECRET_KEY("aws_secret_key", STRING),
    AWS_ACCOUNT_ID("aws_account_id", INTEGER),
    AWS_REGION_ID("aws_region_id", REGION),

    AUTH_ENDPOINT_URI("auth.endpoint.uri", URLL),
    JOB_TIMEOUT("job.timeout", INTEGER),
    JOB_MAXRETRY("job.maxretry", INTEGER),
    SNS_BROADCAST_TOPIC_NAME("sns.broadcast.topic_name", STRING),
    SNS_BROADCAST_VISIBILITY_TIMEOUT("sns.broadcast.visibility_timeout", INTEGER),
    SCORE_FACTOR("score.factor", FLOAT),
    JPA_SHOWSQL("JPA_SHOWSQL", BOOLEAN);



    public enum AllowedType {
        URLL(URL.class) {
            @Override
            void parse(String value) throws MalformedURLException {
                new URL(value);
            }
        }, REGION(Regions.class) {
            @Override
            void parse(String value) {
                Regions.fromName(value);
            }
        }, BOOLEAN(Boolean.class){
            @Override
            void parse(String value) {
                if(!value.trim().toLowerCase().matches("true|false"))
                    throw new IllegalArgumentException("not boolean");
            }
        }, INTEGER(Integer.class){
            @Override
            void parse(String value) {
                Integer.valueOf(value);
            }
        }, FLOAT(Float.class){
            @Override
            void parse(String value) {
                Float.valueOf(value);
            }
        },
        STRING(String.class);

        /**
         * Method is overriden for each type except String. So if value can not be parsed by other types,  type String is accepted
         * @param value
         * @throws Exception
         */
        void parse(String value) throws Exception {
        }

        private Class tClazz;

        AllowedType(Class tClazz) {
            this.tClazz = tClazz;
        }

        public Class getTClazz() {
            return tClazz;
        }

        public boolean isValid(String value) {
            try {
                parse(value == null ? null : value.trim());
                return true;
            } catch (Exception ex) {
                return false;
            }
        }
    }


    private AllowedType type;
    private String origianlName;


    Predefined(String name, AllowedType type) {
        this.type = type;
        this.origianlName = name;
    }

    public String getOriginalName() {
        return origianlName;
    }

    public AllowedType getType() {
        return type;
    }

    @Override
    public String toString() {
        return origianlName + ", " + type;
    }
}



