package send.nutez.model;

public enum Unit {
    g("g"),
    kg("kg"),
    ug("ug"),
    mg("mg"),
    Eper("E%"),
    g_per_bodyweight("g/kg bw"),
    L("l"),
    ml("ml"),
    MJ("MJ"),
    kcal("kcal");

    String unit;
    Unit(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }

    public static Unit getUnit(String unit) {
        if(unit == null)
            return null;
        try {
            return Unit.valueOf("unit");
        } catch (IllegalArgumentException e) {
            try {
                return Unit.valueOf("unit".toLowerCase());
            } catch (IllegalArgumentException e2) {
                switch (unit.toUpperCase()) {
                    case "UG":
                    case "UG RE":
                    case "UG DFE":
                        return ug;
                    case "MG":
                    case "MG NE/MJ":
                    case "MG/MJ":
                        return mg;
                    case "G":
                        return g;
                    case "KG":
                        return kg;
                    case "L":
                        return L;
                    case "ML":
                        return ml;
                    case "KCAL":
                        return kcal;
                    case "MJ":
                        return MJ;
                    case "E%":
                        return Eper;
                    case "g/kg bw":
                    case "G/KG BW":
                        return g_per_bodyweight;
                }
            }
        }
        return null;
    }

    public double convert(Unit to, double value) {
        if(to == null || to.equals(this))
            return value;
        double t = 1000.0;
        if(this.equals(L)) {
            if(to.equals(ml)) {
                return value * t;
            } else if(to.equals(g)) {
                return value * t;
            }
        } else if(this.equals(ml)) {
            if(to.equals(L))
                return value / t;
            else if(to.equals(g)) {
                return value;
            }
        } else if(this.equals(ug)) {
            switch (to) {
                case g:
                    return value / t / t;
                case mg:
                    return value / t;
                case kg:
                    return value / t / t / t;
            }
        } else if(this.equals(mg)) {
            switch (to) {
                case ug:
                    return value * t;
                case g:
                    return value / t;
                case kg:
                    return value / t / t;
            }
        } else if(this.equals(g)) {
            switch (to) {
                case ug:
                    return value * t * t;
                case mg:
                    return value * t;
                case kg:
                case L:
                    return value / t;
            }
        } else if(this.equals(kg)) {
            switch (to) {
                case ug:
                    return value * t * t * t;
                case mg:
                    return value * t * t;
                case g:
                    return value * t;
                case L:
                    return value;
            }
        } else if(this.equals(kcal)) {
            if(to.equals(MJ)) {
                return value * 0.0042;
            }
        } else if(this.equals(MJ)) {
            if(to.equals(kcal))
                return value / 0.0042;
        }
        return value;
    }
}
