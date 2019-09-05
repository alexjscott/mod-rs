databaseChangeLog = {

    changeSet(author: "ethan (generated)", id: "1567683497281-1") {
        createSequence(sequenceName: "hibernate_sequence")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-2") {
        createTable(tableName: "address") {
            column(name: "addr_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "addr_label", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "owner_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-3") {
        createTable(tableName: "address_line") {
            column(name: "al_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "al_seq", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "al_value", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "al_type_rv_fk", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "owner_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-4") {
        createTable(tableName: "address_tag") {
            column(name: "address_tags_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "tag_id", type: "BIGINT")
        }
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-5") {
        createTable(tableName: "announcement") {
            column(name: "ann_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "ann_code", type: "VARCHAR(255)")

            column(name: "ann_expiry_date", type: "timestamp")

            column(name: "ann_announce_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "ann_owner_fk", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "ann_description", type: "VARCHAR(255)")
        }
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-6") {
        createTable(tableName: "custom_property") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "custom_propertyPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "definition_id", type: "VARCHAR(36)")

            column(name: "note", type: "CLOB")

            column(name: "parent_id", type: "BIGINT")
        }
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-7") {
        createTable(tableName: "custom_property_blob") {
            column(name: "id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "value", type: "OID") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-8") {
        createTable(tableName: "custom_property_boolean") {
            column(name: "id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "value", type: "BOOLEAN") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-9") {
        createTable(tableName: "custom_property_container") {
            column(name: "id", type: "BIGINT") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-10") {
        createTable(tableName: "custom_property_decimal") {
            column(name: "id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "value", type: "NUMBER(19, 2)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-11") {
        createTable(tableName: "custom_property_definition") {
            column(name: "pd_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "pd_name", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "pd_primary", type: "BOOLEAN") {
                constraints(nullable: "false")
            }

            column(name: "pd_type", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "pd_label", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "pd_description", type: "VARCHAR(255)")

            column(name: "pd_weight", type: "INT") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-12") {
        createTable(tableName: "custom_property_integer") {
            column(name: "id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "value", type: "INT") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-13") {
        createTable(tableName: "custom_property_refdata") {
            column(name: "id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "value_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-14") {
        createTable(tableName: "custom_property_refdata_definition") {
            column(name: "pd_id", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "category_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-15") {
        createTable(tableName: "custom_property_text") {
            column(name: "id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "value", type: "CLOB") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-16") {
        createTable(tableName: "directory_entry") {
            column(name: "de_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "custom_properties_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "de_slug", type: "VARCHAR(255)")

            column(name: "de_foaf_timestamp", type: "BIGINT")

            column(name: "de_foaf_url", type: "VARCHAR(255)")

            column(name: "de_name", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "de_status_fk", type: "VARCHAR(36)")

            column(name: "de_desc", type: "VARCHAR(255)")

            column(name: "de_parent", type: "VARCHAR(36)")
        }
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-17") {
        createTable(tableName: "directory_entry_tag") {
            column(name: "directory_entry_tags_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "tag_id", type: "BIGINT")
        }
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-18") {
        createTable(tableName: "friend_assertion") {
            column(name: "fa_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "fa_friend_org", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "fa_owner", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-19") {
        createTable(tableName: "naming_authority") {
            column(name: "na_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "na_symbol", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-20") {
        createTable(tableName: "patron_request") {
            column(name: "pr_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "pr_version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "pr_patron_surname", type: "VARCHAR(255)")

            column(name: "pr_date_created", type: "timestamp")

            column(name: "pr_pub_date", type: "VARCHAR(255)")

            column(name: "pr_edition", type: "VARCHAR(255)")

            column(name: "pr_artnum", type: "VARCHAR(255)")

            column(name: "pr_req_inst_symbol", type: "VARCHAR(255)")

            column(name: "pr_doi", type: "VARCHAR(255)")

            column(name: "pr_isbn", type: "VARCHAR(255)")

            column(name: "pr_information_source", type: "VARCHAR(255)")

            column(name: "pr_bici", type: "VARCHAR(255)")

            column(name: "pr_place_of_pub", type: "VARCHAR(255)")

            column(name: "pr_patron_identifier", type: "VARCHAR(255)")

            column(name: "pr_state_fk", type: "VARCHAR(36)")

            column(name: "pr_needed_by", type: "timestamp")

            column(name: "pr_volume", type: "VARCHAR(255)")

            column(name: "pr_title_of_component", type: "VARCHAR(255)")

            column(name: "pr_coden", type: "VARCHAR(255)")

            column(name: "pr_num_pages", type: "VARCHAR(255)")

            column(name: "pr_delay_performing_action_until", type: "timestamp")

            column(name: "pr_stitle", type: "VARCHAR(255)")

            column(name: "pr_patron_reference", type: "VARCHAR(255)")

            column(name: "pr_system_instance_id", type: "VARCHAR(255)")

            column(name: "pr_issue", type: "VARCHAR(255)")

            column(name: "pr_pre_error_status_fk", type: "VARCHAR(36)")

            column(name: "pr_part", type: "VARCHAR(255)")

            column(name: "pr_is_requester", type: "BOOLEAN")

            column(name: "pr_publisher", type: "VARCHAR(255)")

            column(name: "pr_patron_name", type: "VARCHAR(255)")

            column(name: "custom_properties_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "pr_sponsor", type: "VARCHAR(255)")

            column(name: "pr_author_of_component", type: "VARCHAR(255)")

            column(name: "pr_last_updated", type: "timestamp")

            column(name: "pr_rota_position", type: "BIGINT")

            column(name: "pr_pub_type_fk", type: "VARCHAR(36)")

            column(name: "pr_author", type: "VARCHAR(255)")

            column(name: "pr_service_type_fk", type: "VARCHAR(36)")

            column(name: "pr_issn", type: "VARCHAR(255)")

            column(name: "pr_title", type: "VARCHAR(255)")

            column(name: "pr_start_page", type: "VARCHAR(255)")

            column(name: "pr_send_to_patron", type: "BOOLEAN")

            column(name: "pr_eissn", type: "VARCHAR(255)")

            column(name: "pr_pubdate_of_component", type: "VARCHAR(255)")

            column(name: "pr_number_of_retries", type: "INT")

            column(name: "pr_ssn", type: "VARCHAR(255)")

            column(name: "pr_awaiting_protocol_response", type: "BOOLEAN") {
                constraints(nullable: "false")
            }

            column(name: "pr_sici", type: "VARCHAR(255)")

            column(name: "pr_sponsoring_body", type: "VARCHAR(255)")

            column(name: "pr_patron_type", type: "VARCHAR(255)")

            column(name: "pr_quarter", type: "VARCHAR(255)")

            column(name: "pr_sub_title", type: "VARCHAR(255)")
        }
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-21") {
        createTable(tableName: "patron_request_audit") {
            column(name: "pra_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "pra_version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "pra_to_status_fk", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "pra_date_created", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "pra_patron_request_fk", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "pra_from_status_fk", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "pra_duration", type: "BIGINT") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-22") {
        createTable(tableName: "patron_request_rota") {
            column(name: "prr_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "prr_version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "prr_system_identifier", type: "VARCHAR(255)")

            column(name: "prr_date_created", type: "timestamp")

            column(name: "prr_last_updated", type: "timestamp")

            column(name: "prr_protocol_status", type: "BIGINT")

            column(name: "prr_rota_position", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "prr_directory_id_fk", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "prr_patron_request_fk", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "prr_state_fk", type: "VARCHAR(36)")

            column(name: "prr_availability", type: "VARCHAR(255)")

            column(name: "prr_normalised_availability", type: "VARCHAR(255)")

            column(name: "prr_shelfmark", type: "VARCHAR(255)")

            column(name: "prr_available_from", type: "timestamp")
        }
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-23") {
        createTable(tableName: "patron_request_tag") {
            column(name: "patron_request_tags_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "tag_id", type: "BIGINT")
        }
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-24") {
        createTable(tableName: "protocol_conversion") {
            column(name: "pc_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "pc_version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "pc_protocol", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "pc_conversion_value", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "pc_reference_value", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-25") {
        createTable(tableName: "refdata_category") {
            column(name: "rdc_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "rdc_version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "rdc_description", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-26") {
        createTable(tableName: "refdata_value") {
            column(name: "rdv_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "rdv_version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "rdv_value", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "rdv_owner", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "rdv_label", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "class", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-27") {
        createTable(tableName: "service") {
            column(name: "se_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "se_address", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "custom_properties_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "se_name", type: "VARCHAR(255)")

            column(name: "se_type_fk", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "se_business_function_fk", type: "VARCHAR(36)")
        }
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-28") {
        createTable(tableName: "service_account") {
            column(name: "sa_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "sa_account_holder", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "custom_properties_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "sa_service", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "sa_account_details", type: "VARCHAR(255)")
        }
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-29") {
        createTable(tableName: "service_tag") {
            column(name: "service_tags_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "tag_id", type: "BIGINT")
        }
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-30") {
        createTable(tableName: "shipment") {
            column(name: "sh_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "sh_version", type: "BIGINT") {
                constraints(nullable: "false")
            }
            column(name: "sh_date_created", type: "timestamp")
        
            column(name: "sh_last_updated", type: "timestamp")
                        
            column(name: "sh_received_date_fk", type: "BYTEA")

            column(name: "sh_shipment_method_fk", type: "VARCHAR(36)")

            column(name: "sh_directory_entry_fk", type: "VARCHAR(36)")

            column(name: "sh_status_fk", type: "VARCHAR(36)")

            column(name: "sh_ship_date_fk", type: "BYTEA")
        }
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-31") {
        createTable(tableName: "shipment_item") {
            column(name: "si_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "si_version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "si_shipment_fk", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "is_returning", type: "BOOLEAN")

            column(name: "si_patron_request_fk", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-32") {
        createTable(tableName: "state_model") {
            column(name: "sm_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "sm_version", type: "BIGINT") {
                constraints(nullable: "false")
            }
            column(name: "si_date_created", type: "timestamp")
            
            column(name: "si_last_updated", type: "timestamp")

            column(name: "sm_name", type: "VARCHAR(255)")

            column(name: "sm_shortcode", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-33") {
        createTable(tableName: "status") {
            column(name: "st_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "st_version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "st_code", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "st_owner", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-34") {
        createTable(tableName: "symbol") {
            column(name: "sym_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "sym_priority", type: "VARCHAR(255)")

            column(name: "sym_authority_fk", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "sym_owner_fk", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "sym_symbol", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-35") {
        createTable(tableName: "tag") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "tagPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "norm_value", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "value", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-36") {
        createTable(tableName: "tenant_symbol_mapping") {
            column(name: "tsm_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "tsm_version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "tsm_symbol", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "tsm_tenant", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "tsm_block_loopback", type: "BOOLEAN") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-37") {
        addPrimaryKey(columnNames: "addr_id", constraintName: "addressPK", tableName: "address")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-38") {
        addPrimaryKey(columnNames: "al_id", constraintName: "address_linePK", tableName: "address_line")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-39") {
        addPrimaryKey(columnNames: "ann_id", constraintName: "announcementPK", tableName: "announcement")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-40") {
        addPrimaryKey(columnNames: "id", constraintName: "custom_property_blobPK", tableName: "custom_property_blob")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-41") {
        addPrimaryKey(columnNames: "id", constraintName: "custom_property_booleanPK", tableName: "custom_property_boolean")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-42") {
        addPrimaryKey(columnNames: "id", constraintName: "custom_property_containerPK", tableName: "custom_property_container")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-43") {
        addPrimaryKey(columnNames: "id", constraintName: "custom_property_decimalPK", tableName: "custom_property_decimal")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-44") {
        addPrimaryKey(columnNames: "pd_id", constraintName: "custom_property_definitionPK", tableName: "custom_property_definition")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-45") {
        addPrimaryKey(columnNames: "id", constraintName: "custom_property_integerPK", tableName: "custom_property_integer")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-46") {
        addPrimaryKey(columnNames: "id", constraintName: "custom_property_refdataPK", tableName: "custom_property_refdata")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-47") {
        addPrimaryKey(columnNames: "pd_id", constraintName: "custom_property_refdata_definitionPK", tableName: "custom_property_refdata_definition")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-48") {
        addPrimaryKey(columnNames: "id", constraintName: "custom_property_textPK", tableName: "custom_property_text")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-49") {
        addPrimaryKey(columnNames: "de_id", constraintName: "directory_entryPK", tableName: "directory_entry")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-50") {
        addPrimaryKey(columnNames: "fa_id", constraintName: "friend_assertionPK", tableName: "friend_assertion")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-51") {
        addPrimaryKey(columnNames: "na_id", constraintName: "naming_authorityPK", tableName: "naming_authority")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-52") {
        addPrimaryKey(columnNames: "pr_id", constraintName: "patron_requestPK", tableName: "patron_request")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-53") {
        addPrimaryKey(columnNames: "pra_id", constraintName: "patron_request_auditPK", tableName: "patron_request_audit")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-54") {
        addPrimaryKey(columnNames: "prr_id", constraintName: "patron_request_rotaPK", tableName: "patron_request_rota")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-55") {
        addPrimaryKey(columnNames: "pc_id", constraintName: "protocol_conversionPK", tableName: "protocol_conversion")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-56") {
        addPrimaryKey(columnNames: "rdc_id", constraintName: "refdata_categoryPK", tableName: "refdata_category")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-57") {
        addPrimaryKey(columnNames: "rdv_id", constraintName: "refdata_valuePK", tableName: "refdata_value")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-58") {
        addPrimaryKey(columnNames: "se_id", constraintName: "servicePK", tableName: "service")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-59") {
        addPrimaryKey(columnNames: "sa_id", constraintName: "service_accountPK", tableName: "service_account")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-60") {
        addPrimaryKey(columnNames: "sh_id", constraintName: "shipmentPK", tableName: "shipment")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-61") {
        addPrimaryKey(columnNames: "si_id", constraintName: "shipment_itemPK", tableName: "shipment_item")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-62") {
        addPrimaryKey(columnNames: "sm_id", constraintName: "state_modelPK", tableName: "state_model")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-63") {
        addPrimaryKey(columnNames: "st_id", constraintName: "statusPK", tableName: "status")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-64") {
        addPrimaryKey(columnNames: "sym_id", constraintName: "symbolPK", tableName: "symbol")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-65") {
        addPrimaryKey(columnNames: "tsm_id", constraintName: "tenant_symbol_mappingPK", tableName: "tenant_symbol_mapping")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-66") {
        addUniqueConstraint(columnNames: "pd_name", constraintName: "UC_CUSTOM_PROPERTY_DEFINITIONPD_NAME_COL", tableName: "custom_property_definition")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-67") {
        createIndex(indexName: "rdv_entry_idx", tableName: "refdata_value") {
            column(name: "rdv_value")

            column(name: "rdv_owner")
        }
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-68") {
        createIndex(indexName: "td_label_idx", tableName: "custom_property_definition") {
            column(name: "pd_label")
        }
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-69") {
        createIndex(indexName: "td_primary_idx", tableName: "custom_property_definition") {
            column(name: "pd_primary")
        }
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-70") {
        createIndex(indexName: "td_type_idx", tableName: "custom_property_definition") {
            column(name: "pd_type")
        }
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-71") {
        createIndex(indexName: "td_weight_idx", tableName: "custom_property_definition") {
            column(name: "pd_weight")
        }
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-72") {
        addForeignKeyConstraint(baseColumnNames: "de_status_fk", baseTableName: "directory_entry", constraintName: "FK19lypn8h0g8kvr1cke6ddyjwg", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-73") {
        addForeignKeyConstraint(baseColumnNames: "de_parent", baseTableName: "directory_entry", constraintName: "FK1lcdvuk9hkmebm544kwmxoclj", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "de_id", referencedTableName: "directory_entry")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-74") {
        addForeignKeyConstraint(baseColumnNames: "owner_id", baseTableName: "address_line", constraintName: "FK27dakevcmnu3o22tdrpob6npg", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "addr_id", referencedTableName: "address")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-75") {
        addForeignKeyConstraint(baseColumnNames: "custom_properties_id", baseTableName: "directory_entry", constraintName: "FK2qp9dd004mntrub21o6djlxqh", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "custom_property_container")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-76") {
        addForeignKeyConstraint(baseColumnNames: "definition_id", baseTableName: "custom_property", constraintName: "FK36grvth72fb7wu5i5xaeqjitw", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "pd_id", referencedTableName: "custom_property_definition")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-77") {
        addForeignKeyConstraint(baseColumnNames: "se_type_fk", baseTableName: "service", constraintName: "FK37qd0xlyn5tpy48wega3ss3hy", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-78") {
        addForeignKeyConstraint(baseColumnNames: "prr_patron_request_fk", baseTableName: "patron_request_rota", constraintName: "FK44e42grtfehtfv3q11yhbvxl", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "pr_id", referencedTableName: "patron_request")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-79") {
        addForeignKeyConstraint(baseColumnNames: "ann_owner_fk", baseTableName: "announcement", constraintName: "FK4hir8ts72q8qvhr7skxe8wss9", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "de_id", referencedTableName: "directory_entry")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-80") {
        addForeignKeyConstraint(baseColumnNames: "st_owner", baseTableName: "status", constraintName: "FK510qo8iuecwl6gomqmcrwoejg", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "sm_id", referencedTableName: "state_model")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-81") {
        addForeignKeyConstraint(baseColumnNames: "pr_service_type_fk", baseTableName: "patron_request", constraintName: "FK5fk9w5hr3e7s3f2kg9t4acnmm", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-82") {
        addForeignKeyConstraint(baseColumnNames: "value_id", baseTableName: "custom_property_refdata", constraintName: "FK5ogn0fedwxxy4fhmq9du4qej2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-83") {
        addForeignKeyConstraint(baseColumnNames: "tag_id", baseTableName: "patron_request_tag", constraintName: "FK6h11nyf2iuoowopq6o047957x", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "tag")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-84") {
        addForeignKeyConstraint(baseColumnNames: "directory_entry_tags_id", baseTableName: "directory_entry_tag", constraintName: "FK73prfacykqmx20o3gr9dr7b98", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "de_id", referencedTableName: "directory_entry")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-85") {
        addForeignKeyConstraint(baseColumnNames: "pc_protocol", baseTableName: "protocol_conversion", constraintName: "FK7ap6nhx5h49y414ixd87v9yhd", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-86") {
        addForeignKeyConstraint(baseColumnNames: "tag_id", baseTableName: "address_tag", constraintName: "FK8mggv80lsn331xa42585kim18", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "tag")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-87") {
        addForeignKeyConstraint(baseColumnNames: "sh_status_fk", baseTableName: "shipment", constraintName: "FK8p7dgp99ud090qg4todpfxb5p", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-88") {
        addForeignKeyConstraint(baseColumnNames: "pra_patron_request_fk", baseTableName: "patron_request_audit", constraintName: "FK9x1a04d0r113lvv8f7cbxcmxu", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "pr_id", referencedTableName: "patron_request")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-89") {
        addForeignKeyConstraint(baseColumnNames: "pc_reference_value", baseTableName: "protocol_conversion", constraintName: "FK9xvislmhbajmpsasxavur17o3", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-90") {
        addForeignKeyConstraint(baseColumnNames: "pra_from_status_fk", baseTableName: "patron_request_audit", constraintName: "FKabffyd5nlq7y7qnlt5hvgll1i", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "st_id", referencedTableName: "status")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-91") {
        addForeignKeyConstraint(baseColumnNames: "custom_properties_id", baseTableName: "patron_request", constraintName: "FKaeblgdoku7ylgu41p28vbn409", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "custom_property_container")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-92") {
        addForeignKeyConstraint(baseColumnNames: "patron_request_tags_id", baseTableName: "patron_request_tag", constraintName: "FKagafoiedlc7mv2khl13xyfgc1", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "pr_id", referencedTableName: "patron_request")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-93") {
        addForeignKeyConstraint(baseColumnNames: "fa_friend_org", baseTableName: "friend_assertion", constraintName: "FKam7kxpwd75are1h7o0easuo03", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "de_id", referencedTableName: "directory_entry")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-94") {
        addForeignKeyConstraint(baseColumnNames: "sym_owner_fk", baseTableName: "symbol", constraintName: "FKatkxebh688uppornia9wp6u0o", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "de_id", referencedTableName: "directory_entry")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-95") {
        addForeignKeyConstraint(baseColumnNames: "category_id", baseTableName: "custom_property_refdata_definition", constraintName: "FKbrh88caagajlvrpaydg4tr3qx", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdc_id", referencedTableName: "refdata_category")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-96") {
        addForeignKeyConstraint(baseColumnNames: "pr_pub_type_fk", baseTableName: "patron_request", constraintName: "FKc8gk5vkafp9rxv346hvoxrpcy", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-97") {
        addForeignKeyConstraint(baseColumnNames: "prr_state_fk", baseTableName: "patron_request_rota", constraintName: "FKcdhnwi83af93vgvhxq39a4cu0", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "st_id", referencedTableName: "status")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-98") {
        addForeignKeyConstraint(baseColumnNames: "parent_id", baseTableName: "custom_property", constraintName: "FKd5u2tgpracxvk1xw8pdreuj5h", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "custom_property_container")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-99") {
        addForeignKeyConstraint(baseColumnNames: "sym_authority_fk", baseTableName: "symbol", constraintName: "FKgd9iwv5imahohd3irh7a4tysq", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "na_id", referencedTableName: "naming_authority")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-100") {
        addForeignKeyConstraint(baseColumnNames: "sh_shipment_method_fk", baseTableName: "shipment", constraintName: "FKgkow56qifdc59yquh1wgcjiww", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-101") {
        addForeignKeyConstraint(baseColumnNames: "rdv_owner", baseTableName: "refdata_value", constraintName: "FKh4fon2a7k4y8b2sicjm0i6oy8", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdc_id", referencedTableName: "refdata_category")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-102") {
        addForeignKeyConstraint(baseColumnNames: "custom_properties_id", baseTableName: "service_account", constraintName: "FKh8o9kxfjd3rn84sjhf2m8k1kd", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "custom_property_container")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-103") {
        addForeignKeyConstraint(baseColumnNames: "owner_id", baseTableName: "address", constraintName: "FKiscq9dhgj0e6hxlj49ejxavw1", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "de_id", referencedTableName: "directory_entry")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-104") {
        addForeignKeyConstraint(baseColumnNames: "pra_to_status_fk", baseTableName: "patron_request_audit", constraintName: "FKjpyw1slnckaux7ypxrbu21b5r", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "st_id", referencedTableName: "status")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-105") {
        addForeignKeyConstraint(baseColumnNames: "pr_state_fk", baseTableName: "patron_request", constraintName: "FKk4ew47ehoi4whapjey3nyin3h", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "st_id", referencedTableName: "status")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-106") {
        addForeignKeyConstraint(baseColumnNames: "sa_account_holder", baseTableName: "service_account", constraintName: "FKl0sums8w3h2i90a7gudkkvs6", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "de_id", referencedTableName: "directory_entry")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-107") {
        addForeignKeyConstraint(baseColumnNames: "si_shipment_fk", baseTableName: "shipment_item", constraintName: "FKl9tqg5oeklojv0sm755yc8889", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "sh_id", referencedTableName: "shipment")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-108") {
        addForeignKeyConstraint(baseColumnNames: "custom_properties_id", baseTableName: "service", constraintName: "FKlcsx75pv26118e28ske0wgft7", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "custom_property_container")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-109") {
        addForeignKeyConstraint(baseColumnNames: "sa_service", baseTableName: "service_account", constraintName: "FKlw0rgy9jm8bhf9cn2ok7yr76b", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "se_id", referencedTableName: "service")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-110") {
        addForeignKeyConstraint(baseColumnNames: "se_business_function_fk", baseTableName: "service", constraintName: "FKm4goei4gs0kc3o37owkar9qmn", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-111") {
        addForeignKeyConstraint(baseColumnNames: "si_patron_request_fk", baseTableName: "shipment_item", constraintName: "FKmtbbcokkut7sm5w8da1gvmmsk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "pr_id", referencedTableName: "patron_request")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-112") {
        addForeignKeyConstraint(baseColumnNames: "al_type_rv_fk", baseTableName: "address_line", constraintName: "FKnrum0mlrqrdim99tpv2fsrppf", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-113") {
        addForeignKeyConstraint(baseColumnNames: "pr_pre_error_status_fk", baseTableName: "patron_request", constraintName: "FKoitf6o8ntq4hh399cujuwhbxv", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "st_id", referencedTableName: "status")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-114") {
        addForeignKeyConstraint(baseColumnNames: "fa_owner", baseTableName: "friend_assertion", constraintName: "FKq0b79ux6oihg46yoks9vg154c", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "de_id", referencedTableName: "directory_entry")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-115") {
        addForeignKeyConstraint(baseColumnNames: "tag_id", baseTableName: "service_tag", constraintName: "FKq56hgx6qad4r28rntiynnitg8", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "tag")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-116") {
        addForeignKeyConstraint(baseColumnNames: "service_tags_id", baseTableName: "service_tag", constraintName: "FKq58uyhoq6ouyw991t9aps47ka", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "se_id", referencedTableName: "service")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-117") {
        addForeignKeyConstraint(baseColumnNames: "sh_directory_entry_fk", baseTableName: "shipment", constraintName: "FKqxgo143a42gb4njo2f974to9g", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "de_id", referencedTableName: "directory_entry")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-118") {
        addForeignKeyConstraint(baseColumnNames: "address_tags_id", baseTableName: "address_tag", constraintName: "FKsfnxyiyhbwabho720nkg34mjb", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "addr_id", referencedTableName: "address")
    }

    changeSet(author: "ethan (generated)", id: "1567683497281-119") {
        addForeignKeyConstraint(baseColumnNames: "tag_id", baseTableName: "directory_entry_tag", constraintName: "FKt8qbn40lvi5a2hi726uqc5igv", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "tag")
    }
}
