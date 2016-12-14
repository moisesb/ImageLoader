
package br.com.moisesborges.imageloader.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BingImageResponse {

    @SerializedName("_type")
    @Expose
    private String type;
    @SerializedName("instrumentation")
    @Expose
    private Instrumentation instrumentation;
    @SerializedName("webSearchUrl")
    @Expose
    private String webSearchUrl;
    @SerializedName("totalEstimatedMatches")
    @Expose
    private Integer totalEstimatedMatches;
    @SerializedName("value")
    @Expose
    private List<Value> value = null;
    @SerializedName("displayShoppingSourcesBadges")
    @Expose
    private Boolean displayShoppingSourcesBadges;
    @SerializedName("displayRecipeSourcesBadges")
    @Expose
    private Boolean displayRecipeSourcesBadges;
    @SerializedName("similarTerms")
    @Expose
    private List<SimilarTerm> similarTerms = null;

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
     *     The _type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 
     * @return
     *     The instrumentation
     */
    public Instrumentation getInstrumentation() {
        return instrumentation;
    }

    /**
     * 
     * @param instrumentation
     *     The instrumentation
     */
    public void setInstrumentation(Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
    }

    /**
     * 
     * @return
     *     The webSearchUrl
     */
    public String getWebSearchUrl() {
        return webSearchUrl;
    }

    /**
     * 
     * @param webSearchUrl
     *     The webSearchUrl
     */
    public void setWebSearchUrl(String webSearchUrl) {
        this.webSearchUrl = webSearchUrl;
    }

    /**
     * 
     * @return
     *     The totalEstimatedMatches
     */
    public Integer getTotalEstimatedMatches() {
        return totalEstimatedMatches;
    }

    /**
     * 
     * @param totalEstimatedMatches
     *     The totalEstimatedMatches
     */
    public void setTotalEstimatedMatches(Integer totalEstimatedMatches) {
        this.totalEstimatedMatches = totalEstimatedMatches;
    }

    /**
     * 
     * @return
     *     The value
     */
    public List<Value> getValue() {
        return value;
    }

    /**
     * 
     * @param value
     *     The value
     */
    public void setValue(List<Value> value) {
        this.value = value;
    }

    /**
     * 
     * @return
     *     The displayShoppingSourcesBadges
     */
    public Boolean getDisplayShoppingSourcesBadges() {
        return displayShoppingSourcesBadges;
    }

    /**
     * 
     * @param displayShoppingSourcesBadges
     *     The displayShoppingSourcesBadges
     */
    public void setDisplayShoppingSourcesBadges(Boolean displayShoppingSourcesBadges) {
        this.displayShoppingSourcesBadges = displayShoppingSourcesBadges;
    }

    /**
     * 
     * @return
     *     The displayRecipeSourcesBadges
     */
    public Boolean getDisplayRecipeSourcesBadges() {
        return displayRecipeSourcesBadges;
    }

    /**
     * 
     * @param displayRecipeSourcesBadges
     *     The displayRecipeSourcesBadges
     */
    public void setDisplayRecipeSourcesBadges(Boolean displayRecipeSourcesBadges) {
        this.displayRecipeSourcesBadges = displayRecipeSourcesBadges;
    }

    /**
     * 
     * @return
     *     The similarTerms
     */
    public List<SimilarTerm> getSimilarTerms() {
        return similarTerms;
    }

    /**
     * 
     * @param similarTerms
     *     The similarTerms
     */
    public void setSimilarTerms(List<SimilarTerm> similarTerms) {
        this.similarTerms = similarTerms;
    }

}
