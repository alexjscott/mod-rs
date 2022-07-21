databaseChangeLog = {
  changeSet(author: "efreestone (manual)", id: "20220407-1111-001") {
    addColumn(tableName: "request_volume") {
      column(name: "rv_temporary_item_barcode", type: "VARCHAR(52)")
    }
  }

    changeSet(author: "Chas (generated)", id: "1652284970385-1") {
        addColumn(tableName: "action_event_result") {
            column(name: "aer_override_save_state", type: "varchar(36)")
        }

        addForeignKeyConstraint(baseColumnNames: "aer_override_save_state", baseTableName: "action_event_result", constraintName: "FKrb7qtrmsg3hxu3ei59qo0uqel", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "st_id", referencedTableName: "status", validate: "true")
    }

    changeSet(author: "Chas (generated)", id: "1652704268056-1") {
        addColumn(tableName: "status") {
            column(name: "st_stage", type: "varchar(255)")
        }
    }

    changeSet(author: "Chas (generated)", id: "1652795570679-1") {
        addColumn(tableName: "action_event_result") {
            column(name: "aer_from_state", type: "varchar(36)")
        }

        addForeignKeyConstraint(baseColumnNames: "aer_from_state", baseTableName: "action_event_result", constraintName: "FKde3d1w46dlb59n70g1uxsutma", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "st_id", referencedTableName: "status", validate: "true")
    }

    changeSet(author: "Chas", id: "20220606170500-01") {
        // Need a unique index on the 2 fields
        addUniqueConstraint(columnNames: "action_event_result_list_results_id, action_event_result_id", tableName: "action_event_result_list_action_event_result")
    }

    changeSet(author: "Chas", id: "1656061591765-01") {
        // Added the additional fields required for the undo functionality
        addColumn(tableName: "action_event") {
            column(name: "ae_undo_status", type: "varchar(20)")
        }

        addColumn(tableName: "patron_request") {
            column(name: "pr_last_audit_no", type: "int4")
        }

        addColumn(tableName: "patron_request_audit") {
            column(name: "pra_action_event", type: "varchar(36)")
        }

        addForeignKeyConstraint(baseColumnNames: "pra_action_event", baseTableName: "patron_request_audit", constraintName: "FKmme295lu9ylkj66ckku8o6snl", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "ae_id", referencedTableName: "action_event", validate: "true")

        addColumn(tableName: "patron_request_audit") {
            column(name: "pra_audit_no", type: "int4")
        }

        addColumn(tableName: "patron_request_audit") {
            column(name: "pra_message_sequence_no", type: "int4")
        }

        addColumn(tableName: "patron_request_audit") {
            column(name: "pra_rota_position", type: "int8")
        }

        addColumn(tableName: "patron_request_audit") {
            column(name: "pra_undo_performed", type: "boolean")
        }
    }

    changeSet(author: "Chas (generated)", id: "1657099942347-1") {
        createTable(tableName: "report") {
            column(name: "r_id", type: "VARCHAR(36)") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "reportPK")
            }

            column(name: "r_version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "r_date_created", type: "TIMESTAMP WITHOUT TIME ZONE") {
                constraints(nullable: "false")
            }

            column(name: "r_last_updated", type: "TIMESTAMP WITHOUT TIME ZONE") {
                constraints(nullable: "false")
            }

            column(name: "r_name", type: "VARCHAR(64)") {
                constraints(nullable: "false")
            }

            column(name: "r_description", type: "VARCHAR(2000)") {
                constraints(nullable: "false")
            }

            column(name: "r_domain", type: "VARCHAR(64)") {
                constraints(nullable: "false")
            }

            column(name: "r_filename", type: "VARCHAR(512)") {
                constraints(nullable: "false")
            }

            column(name: "r_is_single_record", type: "BOOLEAN") {
                constraints(nullable: "false")
            }

            column(name: "r_report_definition", type: "TEXT") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "jskomorowski", id: "20220624-1745-003") {
        createTable(tableName: "host_lms_item_loan_policy") {
            column(name: "hlilp_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
            column(name: "hlilp_version", type: "BIGINT")
            column(name: "hlilp_code", type: "VARCHAR(255)") { constraints(nullable: "false") }
            column(name: "hlilp_name", type: "VARCHAR(255)")
            column(name: "hlilp_hidden", type: "BOOLEAN") { constraints(nullable: "false") }
            column(name: "hlilp_lendable", type: "BOOLEAN") { constraints(nullable: "false") }
            column(name: "hlilp_date_created", type: "timestamp")
            column(name: "hlilp_last_updated", type: "timestamp")
        }
    }

    changeSet(author: "jskomorowski", id: "20220721-1600-001") {
        addPrimaryKey(columnNames: "hlilp_id", constraintName: "host_lms_item_loan_policyPK", tableName: "host_lms_item_loan_policy")
    }
}
