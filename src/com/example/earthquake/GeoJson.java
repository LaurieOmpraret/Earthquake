package com.example.earthquake;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class GeoJson {
    public class Metadata {
        private Long generated;
        private String url;
        private String api;
        private Integer count;
        private Integer status;
        public Long getGenerated() {
            return generated;
        }
        public String getUrl() {
            return url;
        }
        public String getApi() {
            return api;
        }
        public int getCount() {
            return count;
        }
        public int getStatus() {
            return status;
        }
    }
    public class Seisme {
        public String getTitle() {
            return properties.getTitle();
        }
        public String getStandardTime() {
            Date d = new Date(properties.getTime());
            return d.toString();
        }
        public String getMagString() {
            return Double.toString(properties.getMag());
        }
        public double getMag() {
            return properties.getMag();
        }

        public int getMagImg() {
            double mag = properties.getMag();
            if  (mag <= 4) {
                return R.drawable.circle_green;
            } else if (mag <= 7) {
                return R.drawable.circle_orange;
            } else {
                return R.drawable.circle_red;
            }
        }
        public class Properties {
            private Double mag;
            private String place;
            private Long time;
            private Long updated;
            private Integer tz;
            private String url;
            private String detail;
            private Integer felt;
            private Double cdi;
            private Double mmi;
            private String alert;
            private String status;
            private Integer tsunami;
            private Integer sig;
            private String net;
            private String code;
            private String ids;
            private String sources;
            private String types;
            private Integer nts;
            private Double dmin;
            private Double rms;
            private Double gap;
            private String magType;
            private String type;
            private String title;
            public String getTitle() {
                return title;
            }
            public Double getMag() {
                return mag;
            }
            public String getPlace() {
                return place;
            }
            public Long getTime() {
                return time;
            }
            public Long getUpdated() {
                return updated;
            }
            public Integer getTz() {
                return tz;
            }
            public String getUrl() {
                return url;
            }
            public String getDetail() {
                return detail;
            }
            public Integer getFelt() {
                return felt;
            }
            public Double getCdi() {
                return cdi;
            }
            public Double getMmi() {
                return mmi;
            }
            public String getAlert() {
                return alert;
            }
            public String getStatus() {
                return status;
            }
            public Integer getTsunami() {
                return tsunami;
            }
            public Integer getSig() {
                return sig;
            }
            public String getNet() {
                return net;
            }
            public String getCode() {
                return code;
            }
            public String getIds() {
                return ids;
            }
            public String getSources() {
                return sources;
            }
            public String getTypes() {
                return types;
            }
            public Integer getNts() {
                return nts;
            }
            public Double getDmin() {
                return dmin;
            }
            public Double getRms() {
                return rms;
            }
            public Double getGap() {
                return gap;
            }
            public String getMagType() {
                return magType;
            }
            public String getType() {
                return type;
            }
        }
        public class Geometry {
            private String type;
            private List<Double> coordinates;
        }
        private String id;
        private String type;
        private Properties properties;
        private Geometry geometry;
        public String getId() {
            return id;
        }
        public String getType() {
            return type;
        }
        public Properties getProperties() {
            return properties;
        }
        public Geometry getGeometry() {
            return geometry;
        }
        public Long getTime() {
            return properties.getTime();
        }
    }
    private String type;
    private Metadata metadata;
    private List<Double> bbox;
    private List<Seisme> features;
    public String getType() {
        return type;
    }
    public Metadata getMetadata() {
        return metadata;
    }
    public List<Double> getBbox() {
        return bbox;
    }
    /**
     * @return the earthquakes sorted by anti-chronological dates.
     */
    public List<Seisme> getFeatures() {
        Comparator<Seisme> comparator = new Comparator<Seisme>() {
            public int compare(Seisme c1, Seisme c2) {
                return (int)(long)c2.getTime() - (int)(long)c1.getTime();
            }
        };
        Collections.sort(features, comparator);
        return features;
    }
    public int getCount() {
        return metadata.getCount();
    }
}

