package send.nutez.model;

/*TODO
    CHOLESTEROL,
    SUGARS,
    (Fatty acids, total saturated/monounsaturated/polyunsaturated),
    Total lipid (fat)

 */
public enum Nutrient {
    ALPHA_LINOLENIC_ACID("Alpha-linolenic acid (ALA)"),
    BIOTIN("Biotin"),
    CALCIUM("Calcium"),
    CHLORIDE("Chloride"),
    CHOLINE("Choline"),
    COBALAMIN_VITAMIN_B12("Cobalamin (vitamin B12)"),
    COPPER("Copper"),
    DIETARY_FIBRE("Dietary fibre"),
    EICOSAPENTAENOIC_ACID_DOCOSAHEXAENOIC_ACID_EPA_DHA("Eicosapentaenoic acid, Docosahexaenoic acid (EPA, DHA)"),
    ENERGY("Energy"),
    FLUORIDE("Fluoride"),
    FOLATE("Folate"),
    IODINE("Iodine"),
    IRON("Iron"),
    LINOLEIC_ACID_LA("Linoleic acid (LA)"),
    MAGNESIUM("Magnesium"),
    MANGANESE("Manganese"),
    MOLYBDENUM("Molybdenum"),
    NIACIN("Niacin"),
    PANTOTHENIC_ACID("Pantothenic acid"),
    PHOSPHORUS("Phosphorus"),
    POTASSIUM("Potassium"),
    PROTEIN("Protein"),
    RIBOFLAVIN("Riboflavin"),
    SATURATED_FATTY_ACIDS_SFA("Saturated fatty acids (SFA)"),
    SELENIUM("Selenium"),
    SODIUM("Sodium"),
    THIAMIN("Thiamin"),
    TOTAL_CARBOHYDRATES("Total carbohydrates"),
    TOTAL_FAT("Total fat"),
    TRANS_FATTY_ACIDS_TFA("Trans-fatty acids (TFA)"),
    VITAMIN_A("Vitamin A"),
    VITAMIN_B6("Vitamin B6"),
    VITAMIN_C("Vitamin C"),
    VITAMIN_D("Vitamin D"),
    VITAMIN_E_AS_ALPHA_TOCOPHEROL("Vitamin E as alpha-tocopherol"),
    VITAMIN_K_AS_PHYLLOQUINONE("Vitamin K as phylloquinone"),
    WATER("Water"),
    ZINC("Zinc");

    private String name;

    Nutrient(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
