
package com.twitter;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Medium {

    @Expose
    private Long id;
    @SerializedName("id_str")
    @Expose
    private String idStr;
    @Expose
    private List<Long> indices = new ArrayList<Long>();
    @SerializedName("media_url")
    @Expose
    private String mediaUrl;
    @SerializedName("media_url_https")
    @Expose
    private String mediaUrlHttps;
    @Expose
    private String url;
    @SerializedName("display_url")
    @Expose
    private String displayUrl;
    @SerializedName("expanded_url")
    @Expose
    private String expandedUrl;
    @Expose
    private String type;
    @Expose
    private Sizes sizes;

    /**
     * 
     * @return
     *     The id
     */
    public Long getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The idStr
     */
    public String getIdStr() {
        return idStr;
    }

    /**
     * 
     * @param idStr
     *     The id_str
     */
    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    /**
     * 
     * @return
     *     The indices
     */
    public List<Long> getIndices() {
        return indices;
    }

    /**
     * 
     * @param indices
     *     The indices
     */
    public void setIndices(List<Long> indices) {
        this.indices = indices;
    }

    /**
     * 
     * @return
     *     The mediaUrl
     */
    public String getMediaUrl() {
        return mediaUrl;
    }

    /**
     * 
     * @param mediaUrl
     *     The media_url
     */
    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    /**
     * 
     * @return
     *     The mediaUrlHttps
     */
    public String getMediaUrlHttps() {
        return mediaUrlHttps;
    }

    /**
     * 
     * @param mediaUrlHttps
     *     The media_url_https
     */
    public void setMediaUrlHttps(String mediaUrlHttps) {
        this.mediaUrlHttps = mediaUrlHttps;
    }

    /**
     * 
     * @return
     *     The url
     */
    public String getUrl() {
        return url;
    }

    /**
     * 
     * @param url
     *     The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 
     * @return
     *     The displayUrl
     */
    public String getDisplayUrl() {
        return displayUrl;
    }

    /**
     * 
     * @param displayUrl
     *     The display_url
     */
    public void setDisplayUrl(String displayUrl) {
        this.displayUrl = displayUrl;
    }

    /**
     * 
     * @return
     *     The expandedUrl
     */
    public String getExpandedUrl() {
        return expandedUrl;
    }

    /**
     * 
     * @param expandedUrl
     *     The expanded_url
     */
    public void setExpandedUrl(String expandedUrl) {
        this.expandedUrl = expandedUrl;
    }

    /**
     * 
     * @return
     *     The type
     */
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 
     * @return
     *     The sizes
     */
    public Sizes getSizes() {
        return sizes;
    }

    /**
     * 
     * @param sizes
     *     The sizes
     */
    public void setSizes(Sizes sizes) {
        this.sizes = sizes;
    }

}
