import { Box } from '@mui/material';
import { useTranslate } from '@tolgee/react';
import { Form, Formik } from 'formik';

import { TextField } from 'tg.component/common/form/fields/TextField';
import LoadingButton from 'tg.component/common/form/LoadingButton';
import React from 'react';

export type TAFormData = {
  name: string;
  description: string;
  services: string[];
  url: string;
};

type Props = {
  initialData: Partial<TAFormData>;
  onSubmit: (value: TAFormData) => void;
  loading: boolean | undefined;
  avatarEdit?: React.ReactNode;
};

export function TAEditForm({
  initialData,
  onSubmit,
  loading,
  avatarEdit,
}: Props) {
  const { t } = useTranslate();
  return (
    <Formik
      initialValues={{
        name: initialData.name ?? '',
        description: initialData.description ?? '',
        services: initialData.services?.join('; ') ?? '',
        url: initialData.url ?? '',
      }}
      enableReinitialize
      onSubmit={(values) => {
        onSubmit({
          ...values,
          services: values.services
            .split(';')
            .map((s) => s.trim())
            .filter(Boolean),
        });
      }}
    >
      {() => (
        <Form>
          <Box pt={2}>
            <Box display="grid" gridAutoFlow="column">
              <Box>
                <TextField
                  name="name"
                  size="small"
                  label="Name"
                  fullWidth
                  data-cy="administration-ee-translation-agencies-field-name"
                />
                <TextField
                  name="url"
                  size="small"
                  label="Url"
                  fullWidth
                  data-cy="administration-ee-translation-agencies-field-url"
                />
              </Box>
              {avatarEdit && (
                <Box display="flex" justifyContent="center">
                  {avatarEdit}
                </Box>
              )}
            </Box>
            <TextField
              name="description"
              multiline
              minRows={2}
              size="small"
              label="Description (Markdown)"
              fullWidth
              sx={{ mb: 2 }}
              data-cy="administration-ee-translation-agencies-field-description"
            />
            <TextField
              name="services"
              multiline
              minRows={2}
              size="small"
              label='Services (separated by ";")'
              fullWidth
              sx={{ mb: 2 }}
              data-cy="administration-ee-translation-agencies-field-services"
            />
          </Box>
          <Box display="flex" justifyContent="end" mt={4}>
            <LoadingButton
              loading={loading}
              variant="contained"
              color="primary"
              type="submit"
              data-cy="administration-ee-plan-submit-button"
            >
              {t('global_form_save')}
            </LoadingButton>
          </Box>
        </Form>
      )}
    </Formik>
  );
}
