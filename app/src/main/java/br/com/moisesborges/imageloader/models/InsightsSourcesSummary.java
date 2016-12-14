
package br.com.moisesborges.imageloader.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InsightsSourcesSummary {

    @SerializedName("shoppingSourcesCount")
    @Expose
    private Integer shoppingSourcesCount;
    @SerializedName("recipeSourcesCount")
    @Expose
    private Integer recipeSourcesCount;

    /**
     * 
     * @return
     *     The shoppingSourcesCount
     */
    public Integer getShoppingSourcesCount() {
        return shoppingSourcesCount;
    }

    /**
     * 
     * @param shoppingSourcesCount
     *     The shoppingSourcesCount
     */
    public void setShoppingSourcesCount(Integer shoppingSourcesCount) {
        this.shoppingSourcesCount = shoppingSourcesCount;
    }

    /**
     * 
     * @return
     *     The recipeSourcesCount
     */
    public Integer getRecipeSourcesCount() {
        return recipeSourcesCount;
    }

    /**
     * 
     * @param recipeSourcesCount
     *     The recipeSourcesCount
     */
    public void setRecipeSourcesCount(Integer recipeSourcesCount) {
        this.recipeSourcesCount = recipeSourcesCount;
    }

}
