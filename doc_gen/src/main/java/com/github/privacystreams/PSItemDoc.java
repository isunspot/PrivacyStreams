package com.github.privacystreams;

import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.AnnotationTypeDoc;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by yuanchun on 11/03/2017.
 */

public class PSItemDoc {

    ClassDoc classDoc;

    String name;
    String description;

    List<PSItemFieldDoc> itemFieldDocs;
    List<PSOperatorDoc> providerDocs;

    private PSItemDoc(ClassDoc classDoc) {
        this.classDoc = classDoc;
        this.name = classDoc.name();
        this.description = classDoc.commentText();
        this.itemFieldDocs = new ArrayList<>();
        this.providerDocs = new ArrayList<>();

        List<FieldDoc> allFields = new ArrayList<>();
        this.getAllFieldDocs(classDoc, allFields);

        for (FieldDoc fieldDoc : allFields) {
            PSItemFieldDoc itemFieldDoc = PSItemFieldDoc.build(this, fieldDoc);
            if (itemFieldDoc != null) this.itemFieldDocs.add(itemFieldDoc);
        }

        for (MethodDoc methodDoc : classDoc.methods()) {
            PSOperatorDoc providerDoc = PSOperatorDoc.build(classDoc, methodDoc);
            if (providerDoc != null) this.providerDocs.add(providerDoc);
        }
    }

    private void getAllFieldDocs(ClassDoc classDoc, List<FieldDoc> fieldDocs) {
        if (classDoc.superclass() != null) {
            this.getAllFieldDocs(classDoc.superclass(), fieldDocs);
        }
        if (isValidPSItem(classDoc)) {
            fieldDocs.addAll(Arrays.asList(classDoc.fields()));
        }
    }

    public static PSItemDoc build(ClassDoc classDoc) {
        if (isValidPSItem(classDoc)) {
            return new PSItemDoc(classDoc);
        }
        return null;
    }

    public static boolean isValidPSItem(ClassDoc classDoc) {
        AnnotationDesc[] annotations = classDoc.annotations();
        for (AnnotationDesc annotation : annotations) {
            AnnotationTypeDoc annotationType = annotation.annotationType();
            if (Consts.ITEM_ANNOTATION.equals(annotationType.toString())) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        String itemDocStr = "";
        itemDocStr += "## " + this.name + "\n";
        itemDocStr += "Package: `" + this.classDoc.containingPackage() + "`\n\n";
        itemDocStr += this.description + "\n";
        itemDocStr += "### Fields\n";
        itemDocStr += "| Reference | Name | Type | Description |\n";
        itemDocStr += "|----|----|----|----|\n";

        for (PSItemFieldDoc itemFieldDoc : this.itemFieldDocs) {
            itemDocStr += itemFieldDoc.toString() + "\n";
        }

        itemDocStr += "\n### Providers\n";
        itemDocStr += "| Type | Signature & Description |\n";
        itemDocStr += "|----|----|\n";

        for (PSOperatorDoc providerDoc : this.providerDocs) {
            itemDocStr += providerDoc.toString() + "\n";
        }

        return itemDocStr;
    }

}
