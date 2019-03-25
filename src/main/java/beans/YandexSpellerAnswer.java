
package beans;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class YandexSpellerAnswer {

    @SerializedName("code")
    @Expose
    public Integer code;
    @SerializedName("pos")
    @Expose
    public Integer pos;
    @SerializedName("row")
    @Expose
    public Integer row;
    @SerializedName("col")
    @Expose
    public Integer col;
    @SerializedName("len")
    @Expose
    public Integer len;
    @SerializedName("word")
    @Expose
    public String word;
    @SerializedName("s")
    @Expose
    public List<String> s;

    public YandexSpellerAnswer(Integer code, String word, List<String> s) {
        this.code = code;
        this.word = word;
        this.s = s;
        this.col = 0;
        this.len = word.length();
        this.pos = 0;
        this.row = 0;
    }

    public YandexSpellerAnswer(Integer code, Integer pos, Integer row, Integer col, Integer len, String word, List<String> s) {
        this.code = code;
        this.pos = pos;
        this.row = row;
        this.col = col;
        this.len = len;
        this.word = word;
        this.s = s;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("code", code).append("pos", pos).append("row", row).append("col", col).append("len", len).append("word", word).append("s", s).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(col).append(code).append(s).append(len).append(pos).append(row).append(word).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof YandexSpellerAnswer) == false) {
            return false;
        }
        YandexSpellerAnswer rhs = ((YandexSpellerAnswer) other);
        return new EqualsBuilder().append(col, rhs.col).append(code, rhs.code).append(s, rhs.s).append(len, rhs.len).append(pos, rhs.pos).append(row, rhs.row).append(word, rhs.word).isEquals();
    }

}
